package io.vertx.criptobuyer.models;

import io.vertx.criptobuyer.entities.Account;
import io.vertx.skeleton.models.PersistedRecord;
import io.vertx.skeleton.models.RepositoryRecord;
import io.vertx.skeleton.models.Tenant;

import java.math.BigDecimal;

public record PersistedAccount(
  String accountId,
  BigDecimal balance,
  BigDecimal reservedAmount,
  PersistedRecord persistedRecord
) implements RepositoryRecord<PersistedAccount> {

  public static PersistedAccount from(Account account) {
    return new PersistedAccount(account.accountId(), account.balance(), account.reservedAmount(), PersistedRecord.newRecord(Tenant.unknown()));
  }

  @Override
  public PersistedAccount with(PersistedRecord persistedRecord) {
    return new PersistedAccount(accountId, balance, reservedAmount, persistedRecord);
  }

  public Account map() {
    return new Account(accountId, balance, reservedAmount);
  }


  public PersistedAccount update(Account newState) {
    return new PersistedAccount(accountId, newState.balance(), newState.reservedAmount(), persistedRecord);
  }
}
