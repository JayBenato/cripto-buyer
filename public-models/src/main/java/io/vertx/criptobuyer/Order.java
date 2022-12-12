package io.vertx.criptobuyer;


import java.math.BigDecimal;

public record Order(
  String orderId,
  String accountId,
  BigDecimal priceLimit,
  BigDecimal amount,
  OrderState state
) {
}
