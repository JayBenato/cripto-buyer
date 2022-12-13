package io.vertx.cryptobuyer.entities;


import io.vertx.cryptobuyer.commands.CreateOrderCommand;
import io.vertx.cryptobuyer.objects.OrderState;

import java.math.BigDecimal;
import java.util.UUID;

public record Order(
  String orderId,
  String accountId,
  BigDecimal priceLimit,
  Integer amount,
  OrderState state
) {
  public Order processed() {
    return new Order(orderId, accountId, priceLimit, amount, OrderState.PROCESSED);
  }

  public Order fail() {
    return new Order(orderId, accountId, priceLimit, amount, OrderState.FAILED);
  }
  public static Order create(CreateOrderCommand command) {
    return new Order(UUID.randomUUID().toString(),command.accountId(),command.priceLimit(),command.amount(), OrderState.CREATED);
  }
}
