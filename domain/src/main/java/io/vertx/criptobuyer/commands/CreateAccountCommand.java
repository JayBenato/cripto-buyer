package io.vertx.criptobuyer.commands;

import java.math.BigDecimal;

public record CreateAccountCommand(
  String accountId,
  BigDecimal initialBalance
) {
}
