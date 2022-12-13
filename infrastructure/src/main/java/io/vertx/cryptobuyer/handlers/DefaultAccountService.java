package io.vertx.cryptobuyer.handlers;


import io.smallrye.mutiny.Uni;
import io.vertx.cryptobuyer.commands.CreateAccountCommand;
import io.vertx.cryptobuyer.interfaces.AccountService;
import io.vertx.cryptobuyer.entities.Account;
import io.vertx.cryptobuyer.repository.models.PersistedAccount;
import io.vertx.cryptobuyer.repository.models.PersistedAccountKey;
import io.vertx.cryptobuyer.repository.models.PersistedAccountQuery;
import io.vertx.skeleton.orm.Repository;


public class DefaultAccountService implements AccountService {
  private final Repository<PersistedAccountKey, PersistedAccount, PersistedAccountQuery> accounts;

  public DefaultAccountService(Repository<PersistedAccountKey, PersistedAccount, PersistedAccountQuery> accounts) {
    this.accounts = accounts;
  }

  @Override
  public Uni<Account> createAccount(CreateAccountCommand command) {
    final var account = Account.create(command);
    return accounts.insert(PersistedAccount.from(account)).replaceWith(account);
  }

}
