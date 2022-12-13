package io.vertx.criptobuyer.commands;

import java.math.BigDecimal;
import java.util.Objects;

public record CreateOrderCommand(
  String accountId,
  BigDecimal priceLimit,
  Integer amount
) {
  public CreateOrderCommand {
    if (amount <= 0) {
      throw new IllegalArgumentException("invalid amount - amount should be greater than 0");
    }
  }
}
