package io.vertx.criptobuyer.entities;


import io.vertx.criptobuyer.commands.PurchaseBtcCommand;
import io.vertx.criptobuyer.commands.CreateAccountCommand;
import io.vertx.criptobuyer.errors.InsufficientBalanceException;
import io.vertx.criptobuyer.errors.ReservationLimitException;

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
