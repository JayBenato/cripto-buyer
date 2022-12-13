package io.vertx.cryptobuyer.interfaces;

import io.smallrye.mutiny.Uni;
import io.vertx.cryptobuyer.entities.Account;
import io.vertx.cryptobuyer.commands.CreateAccountCommand;


public interface AccountService {

  Uni<Account> createAccount(CreateAccountCommand command);

}
