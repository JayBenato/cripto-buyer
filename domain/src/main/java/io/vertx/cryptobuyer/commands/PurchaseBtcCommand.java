package io.vertx.cryptobuyer.commands;

import io.vertx.cryptobuyer.entities.Order;

import java.math.BigDecimal;

public record PurchaseBtcCommand(
  BigDecimal currentPrice,
  Order order
) {
}
