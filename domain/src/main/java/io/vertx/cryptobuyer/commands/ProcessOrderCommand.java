package io.vertx.cryptobuyer.commands;

import io.vertx.cryptobuyer.entities.Order;

public record ProcessOrderCommand(
  Order order
) {
}
