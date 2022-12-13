package io.vertx.criptobuyer.interfaces;

import io.smallrye.mutiny.Uni;
import io.vertx.criptobuyer.entities.Account;
import io.vertx.criptobuyer.commands.CreateAccountCommand;


public interface AccountService {

  Uni<Account> createAccount(CreateAccountCommand command);

}
