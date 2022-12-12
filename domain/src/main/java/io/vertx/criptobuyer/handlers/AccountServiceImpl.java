package io.vertx.criptobuyer.handlers;

import io.smallrye.mutiny.Uni;
import io.vertx.criptobuyer.Account;
import io.vertx.criptobuyer.AccountService;
import io.vertx.criptobuyer.models.*;
import io.vertx.skeleton.models.*;
import io.vertx.skeleton.orm.Repository;

public class AccountServiceImpl implements AccountService {

  private final Repository<PersistedAccountKey, PersistedAccount, PersistedAccountQuery> accounts;

  public AccountServiceImpl(
    Repository<PersistedAccountKey, PersistedAccount, PersistedAccountQuery> accounts
  ) {
    this.accounts = accounts;
  }


  @Override
  public Uni<Void> createAccount(Account account) {
    return accounts.insert(
        new PersistedAccount(account.accountId(), account.balance(), PersistedRecord.newRecord(Tenant.unknown()))
      )
      .replaceWithVoid();
  }

}
