package io.vertx.cryptobuyer.handlers;

import io.smallrye.mutiny.Uni;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.cryptobuyer.commands.ProcessOrderCommand;
import io.vertx.cryptobuyer.commands.PurchaseBtcCommand;
import io.vertx.cryptobuyer.interfaces.OrderExchange;
import io.vertx.cryptobuyer.repository.models.*;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.skeleton.ccp.SingleProcessConsumer;
import io.vertx.skeleton.ccp.models.QueueConfiguration;
import io.vertx.skeleton.ccp.models.QueueType;
import io.vertx.skeleton.orm.Repository;

/**
 * this is a message queue implemented in postgres using pub/sub channels but could be anything (rabbitmq)
 */
public class OrderProcessor implements SingleProcessConsumer<PurchaseBtcCommand, Void> {


  private static final Logger LOGGER = LoggerFactory.getLogger(OrderProcessor.class);
  public static final String ORDER_QUEUE = "order_queue";

  private final Repository<PersistedOrderKey, PersistedOrder, PersistedOrderQuery> orders;
  private final Repository<PersistedAccountKey, PersistedAccount, PersistedAccountQuery> accounts;
  private final OrderExchange orderExchange;

  public OrderProcessor(
    final Repository<PersistedOrderKey, PersistedOrder, PersistedOrderQuery> orders,
    final Repository<PersistedAccountKey, PersistedAccount, PersistedAccountQuery> accounts,
    final OrderExchange exchange
  ) {
    this.orders = orders;
    this.orderExchange = exchange;
    this.accounts = accounts;
  }

  @Override
  public Uni<Void> process(PurchaseBtcCommand command, SqlConnection sqlConnection) {
    LOGGER.info("Processing command -> " + JsonObject.mapFrom(command).encodePrettily());
    return accounts.selectByKey(new PersistedAccountKey(command.order().accountId()), sqlConnection)
      .flatMap(persistedAccount -> {
          final var account = persistedAccount.map();
          final var accountResult = account.purchaseBtc(command);
          final var orderResult = orderExchange.process(new ProcessOrderCommand(command.order()));
          return accounts.updateByKey(persistedAccount.update(accountResult), sqlConnection)
            .flatMap(avoid -> orders.updateByKey(
                PersistedOrder.from(orderResult),
                sqlConnection
              )
            );
        }
      )
      .replaceWithVoid();
  }


  @Override
  public QueueConfiguration configuration() {
    return new QueueConfiguration()
      .setBatchSize(50L)
      .setBootstrapQueue(true)
      .setConcurrency(50)
      .setIdempotency(true)
      .setIdempotencyNumberOfDays(null)
      .setMaintenanceEvery(5L)
      .setMaxRetry(10)
      .setMaxProcessingTimeInMinutes(5L)
      .setQueueName(ORDER_QUEUE)
      .setQueueType(QueueType.PG_QUEUE)
      .setCircuitBreakerMaxFailuer(10);
  }
}
