package io.vertx.criptobuyer.models;

import io.vertx.criptobuyer.Account;
import io.vertx.skeleton.models.PersistedRecord;
import io.vertx.skeleton.models.RepositoryRecord;

import java.math.BigDecimal;

public record PersistedAccount(
  String accountId,
  BigDecimal balance,
  PersistedRecord persistedRecord
) implements RepositoryRecord<PersistedAccount> {

  @Override
  public PersistedAccount with(PersistedRecord persistedRecord) {
    return new PersistedAccount(accountId, balance, persistedRecord);
  }

  public Account map() {
    return new Account(accountId, balance);
  }

  public PersistedAccount deductFromBalance(BigDecimal amount) {
    return new PersistedAccount(accountId, balance.subtract(amount), persistedRecord);
  }
}
