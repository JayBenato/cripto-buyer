package io.vertx.cryptobuyer;

import io.activej.inject.annotation.Provides;
import io.vertx.cryptobuyer.handlers.BtcPriceConsumer;
import io.vertx.skeleton.framework.Verticle;
import io.vertx.skeleton.utils.VertxComponent;

public class Verticles extends VertxComponent {

  @Provides
  Verticle btcPriceConsumer() {
    return new BtcPriceConsumer();
  }

}
