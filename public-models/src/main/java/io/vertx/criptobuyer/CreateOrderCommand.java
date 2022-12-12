package io.vertx.criptobuyer;

import java.math.BigDecimal;

public record CreateOrderCommand(
  String accountId,
  BigDecimal priceLimit,
  BigDecimal amount
) {
}
