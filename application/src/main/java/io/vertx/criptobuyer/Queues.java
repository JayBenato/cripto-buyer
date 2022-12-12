package io.vertx.criptobuyer;

import io.activej.inject.annotation.Inject;
import io.activej.inject.annotation.Provides;
import io.vertx.criptobuyer.handlers.OrderProcessor;
import io.vertx.skeleton.utils.VertxComponent;

public class Queues extends VertxComponent {

  @Provides
  @Inject
  OrderProcessor orderProcessor() {
    return new OrderProcessor();
  }
}
