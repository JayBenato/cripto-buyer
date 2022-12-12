package io.vertx.criptobuyer.handlers;

import io.smallrye.mutiny.Uni;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.impl.cpu.CpuCoreSensor;
import io.vertx.criptobuyer.Order;
import io.vertx.skeleton.framework.Verticle;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.criptobuyer.mappers.OrderMapper;
import io.vertx.criptobuyer.models.*;
import io.vertx.skeleton.ccp.models.Message;
import io.vertx.skeleton.ccp.producers.PgQueueProducer;
import io.vertx.skeleton.models.OrmNotFoundException;
import io.vertx.skeleton.models.Tenant;
import io.vertx.skeleton.orm.Repository;
import io.vertx.skeleton.orm.RepositoryHandler;
import io.vertx.sqlclient.data.Numeric;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class BtcPriceConsumer extends Verticle {

  // this will transform order's into entries in the processing queue concurrently, it scales quite well too. It keeps polling on order's until there's none with limit lower than current price
  // the idea is to submit matching order's to the processing queue as fast as possible

  //  problems with this approach :
  // 1) up-stream publisher may overload consumer's since there's no backpressure mechanism in place (besides the max message buff on vert.x)
  // 2) node dying before submitting, new messages to the processing queue, in a real scenario a recovery mechanism should be implemented leveraging that fact that the downstream consumer is idempotent.

  // possible improvements :
  // 1) isolate non-handled order's in a different table in order to maintain table size stable
  // 2) change order's read flow by making use of a document indexer, something like lucene
  // 3) change order's write flow by using a data-grid infrastructure configured with write-behind strategy
  private Repository<PersistedOrderKey, PersistedOrder, PersistedOrderQuery> repository;

  private PgQueueProducer<Order> queueProducer;
  private static final Logger LOGGER = LoggerFactory.getLogger(BtcPriceConsumer.class);

  @Override
  public DeploymentOptions options() {
    return new DeploymentOptions().setInstances(CpuCoreSensor.availableProcessors() * 2);
  }

  @Override
  public Supplier<io.vertx.core.Verticle> supplier() {
    return BtcPriceConsumer::new;
  }

  @Override
  public Uni<Void> asyncStart() {
    this.repository = new Repository<>(OrderMapper.INSTANCE, RepositoryHandler.leasePool(config(), vertx));
    this.queueProducer = new PgQueueProducer<>(repository.repositoryHandler(), OrderProcessor.ORDER_QUEUE);
    return vertx.eventBus().<String>consumer(BtcPriceConsumer.class.getName())
      .handler(
        currentBtcPrice -> pollUntilDone(new BigDecimal(currentBtcPrice.body()))
          .subscribe()
          .with(
            avoid -> LOGGER.info("Order submitted to processing queue"),
            throwable -> {
              if (throwable instanceof OrmNotFoundException) {
                LOGGER.info("No order's have limit lower than " + currentBtcPrice.body());
              } else {
                LOGGER.error("Unable to submit order's to processing queue");
              }
            }
          )
      )
      .exceptionHandler(this::droppedException)
      .completionHandler();
  }


  private Uni<Void> pollUntilDone(BigDecimal currentBtcOPrice) {
    LOGGER.info("Polling orders...");
    return pollOnce(currentBtcOPrice)
      .flatMap(orders -> {
          LOGGER.info("Order's with limit within the current market price of " + currentBtcOPrice + " -> " + new JsonArray(orders).encodePrettily());
          final var messages = orders.stream().map(PersistedOrder::map)
            .map(order -> new Message<>(
              order.orderId(),
              Tenant.unknown(),
              null,
              null,
              0,
              order
            ))
            .toList();
          return queueProducer.enqueue(messages).replaceWithVoid();

        }
      )
      .flatMap(avoid -> pollUntilDone(currentBtcOPrice));
  }


  private Uni<List<PersistedOrder>> pollOnce(BigDecimal currentBtcPrice) {
    final var consumeOrdersStatement = pollingStatement();
    final var batchSize = 50L;
    return repository.query(consumeOrdersStatement, Map.of("currentBtcPrice", Numeric.parse(currentBtcPrice.toPlainString()), "batchSize", batchSize));
  }


  private static String pollingStatement() {
    return """
      update orders set state = 'WAITING_FOR_PRICE_LIMOrder's with limit lower thanIT' where id in (
      select id from orders where price_limit >= #{currentBtcPrice} and state = 'CREATED'
      for update skip locked limit 50
      ) returning *;
      """;
  }

  private void droppedException(Throwable throwable) {
    LOGGER.error("Consumer had to drop ", throwable);
  }

  @Override
  public Uni<Void> asyncStop() {
    return repository.repositoryHandler().shutDown();
  }
}
