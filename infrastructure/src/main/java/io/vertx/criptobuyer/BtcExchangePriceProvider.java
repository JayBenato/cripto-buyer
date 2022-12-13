package io.vertx.criptobuyer;

import io.smallrye.mutiny.Uni;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.criptobuyer.interfaces.PriceProvider;
import io.vertx.mutiny.core.Vertx;
import io.vertx.skeleton.task.SynchronizationStrategy;
import io.vertx.skeleton.task.SynchronizedTask;


public class BtcExchangePriceProvider implements SynchronizedTask {

  private final Vertx vertx;
  private final PriceProvider priceProvider;
  private static final Logger LOGGER = LoggerFactory.getLogger(BtcExchangePriceProvider.class);

  public BtcExchangePriceProvider(Vertx vertx, PriceProvider priceProvider) {
    this.vertx = vertx;
    this.priceProvider = priceProvider;
  }

  private void handleException(Throwable throwable) {
    LOGGER.error("Connection dropped exception", throwable);
  }

  @Override
  public Uni<Void> performTask() {
    // NOTE : I understand message delivery in vert.x is best-effort, in a real scenario a proper message broker could be used.
    LOGGER.info("Fetching BTC price");
    return priceProvider.currentPrice()
      .map(price -> {
          LOGGER.info("Current price -> " + price);
          return vertx.eventBus().publish(BtcPriceConsumer.class.getName(), price.toString());
        }
      )
      .replaceWithVoid();
  }

  @Override
  public SynchronizationStrategy strategy() {
    return SynchronizationStrategy.LOCAL;
  }
}
