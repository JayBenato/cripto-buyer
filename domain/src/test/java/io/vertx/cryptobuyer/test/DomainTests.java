package io.vertx.cryptobuyer.test;

import io.vertx.cryptobuyer.commands.PurchaseBtcCommand;
import io.vertx.cryptobuyer.commands.CreateAccountCommand;
import io.vertx.cryptobuyer.commands.CreateOrderCommand;
import io.vertx.cryptobuyer.errors.InsufficientBalanceException;
import io.vertx.cryptobuyer.entities.Account;
import io.vertx.cryptobuyer.entities.Order;
import io.vertx.junit5.VertxExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.util.UUID;

@ExtendWith(VertxExtension.class)
public class DomainTests {


  @Test
  void insufficientBalance() {
    final var account = Account.create(new CreateAccountCommand(UUID.randomUUID().toString(), new BigDecimal(0)));
    final var order = Order.create(new CreateOrderCommand(account.accountId(), new BigDecimal(10), 1));
    Assertions.assertThrows(InsufficientBalanceException.class, () -> account.purchaseBtc(new PurchaseBtcCommand(new BigDecimal(10), order)));
  }

  @Test
  void testBtcPurchase() {
    final var account = Account.create(new CreateAccountCommand(UUID.randomUUID().toString(), new BigDecimal(10)));
    Assertions.assertEquals(new BigDecimal(10),account.balance());
    Assertions.assertEquals(new BigDecimal(0),account.reservedAmount());

    final var order = Order.create(new CreateOrderCommand(account.accountId(), new BigDecimal(10), 1));

    final var accountAfterReserve = account.reserve(order);
    Assertions.assertEquals(new BigDecimal(10), accountAfterReserve.reservedAmount());
    Assertions.assertEquals(new BigDecimal(10), accountAfterReserve.balance());

    final var accountAfterPurchase = accountAfterReserve.purchaseBtc(new PurchaseBtcCommand(new BigDecimal(9), order));
    Assertions.assertEquals(new BigDecimal(1), accountAfterPurchase.balance());
    Assertions.assertEquals(new BigDecimal(0), accountAfterPurchase.reservedAmount());
  }


}
