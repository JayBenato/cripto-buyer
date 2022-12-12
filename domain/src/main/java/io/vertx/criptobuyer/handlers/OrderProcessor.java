package io.vertx.criptobuyer.handlers;

import io.smallrye.mutiny.Uni;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.criptobuyer.Order;
import io.vertx.skeleton.ccp.SingleProcessConsumer;
import io.vertx.skeleton.ccp.models.QueueConfiguration;
import io.vertx.skeleton.ccp.models.QueueType;

public class OrderProcessor implements SingleProcessConsumer<Order,Void> {


  private static final Logger LOGGER = LoggerFactory.getLogger(OrderProcessor.class);
  public static final String ORDER_QUEUE = "order_queue";

  @Override
  public Uni<Void> process(Order order) {
    LOGGER.info("Order processed -> " + order.orderId());
    // if this was real it would be bounded to a transaction where the first step would be transform order into btc
    // second part put btc in account
    return Uni.createFrom().voidItem();
  }

  @Override
  public QueueConfiguration configuration() {
    // this is a message queue implemented in postgres using pub/sub channels but could be anything e.g rabbitmq
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
