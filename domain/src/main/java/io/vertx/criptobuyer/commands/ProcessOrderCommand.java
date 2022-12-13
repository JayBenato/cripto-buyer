package io.vertx.criptobuyer.commands;

import io.vertx.criptobuyer.entities.Order;

public record ProcessOrderCommand(
  Order order
) {
}
