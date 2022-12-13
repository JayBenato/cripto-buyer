package io.vertx.criptobuyer.commands;

import io.vertx.criptobuyer.entities.Order;

import java.math.BigDecimal;

public record PurchaseBtcCommand(
  BigDecimal currentPrice,
  Order order
) {
}
