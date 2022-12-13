package io.vertx.cryptobuyer.entities;


import io.vertx.cryptobuyer.commands.PurchaseBtcCommand;
import io.vertx.cryptobuyer.commands.CreateAccountCommand;
import io.vertx.cryptobuyer.errors.InsufficientBalanceException;
import io.vertx.cryptobuyer.errors.ReservationLimitException;

import java.math.BigDecimal;

public record Account(
  String accountId,
  BigDecimal balance,
  BigDecimal reservedAmount
) {


  public static Account create(CreateAccountCommand command) {
    return new Account(
      command.accountId(),
      command.initialBalance(),
      new BigDecimal(0)
    );
  }

  public Account reserve(Order order) {
    final var amountToReserve = order.priceLimit().multiply(new BigDecimal(order.amount()));
    final var newReserveAmount = reservedAmount.add(amountToReserve);
    final var balanceSimulation = balance.subtract(newReserveAmount);
    if (balanceSimulation.compareTo(new BigDecimal(0)) < 0) {
      throw new ReservationLimitException("Reservations exceed current balance ! [maxOrderValue = " + amountToReserve + "] [currentBalance = " + balance + "] [balanceAfterSimulation = " + balanceSimulation + "] [currentReservedAmount = " + reservedAmount + "]");
    }
    return new Account(accountId, balance, newReserveAmount);
  }

  public Account purchaseBtc(PurchaseBtcCommand command) {
    final var purchaseAmount = command.currentPrice().multiply(new BigDecimal(command.order().amount()));
    if (balance.subtract(purchaseAmount).compareTo(new BigDecimal(0)) < 0) {
      throw new InsufficientBalanceException("Insufficient balance, trying to buy order for " + purchaseAmount + " with a balance of only " + balance);
    }
    final var newReservedAmount = reservedAmount.subtract(command.order().priceLimit().multiply(new BigDecimal(command.order().amount())));
    return new Account(accountId, balance.subtract(purchaseAmount), newReservedAmount);
  }
}
