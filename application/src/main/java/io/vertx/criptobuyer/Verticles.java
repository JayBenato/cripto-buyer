package io.vertx.criptobuyer;

import io.activej.inject.annotation.Provides;
import io.vertx.skeleton.framework.Verticle;
import io.vertx.skeleton.utils.VertxComponent;

public class Verticles extends VertxComponent {

  @Provides
  Verticle btcPriceConsumer() {
    return new BtcPriceConsumer();
  }

}
