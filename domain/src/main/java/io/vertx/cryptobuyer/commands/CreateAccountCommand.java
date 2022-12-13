package io.vertx.cryptobuyer.commands;

import java.math.BigDecimal;

public record CreateAccountCommand(
  String accountId,
  BigDecimal initialBalance
) {
}
