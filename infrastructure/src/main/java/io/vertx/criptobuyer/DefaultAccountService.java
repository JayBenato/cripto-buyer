package io.vertx.criptobuyer;


import io.smallrye.mutiny.Uni;
import io.vertx.criptobuyer.commands.CreateAccountCommand;
import io.vertx.criptobuyer.interfaces.AccountService;
import io.vertx.criptobuyer.entities.Account;
import io.vertx.criptobuyer.models.PersistedAccount;
import io.vertx.criptobuyer.models.PersistedAccountKey;
import io.vertx.criptobuyer.models.PersistedAccountQuery;
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
