package io.vertx.criptobuyer.routes;

import io.vertx.criptobuyer.interfaces.AccountService;
import io.vertx.criptobuyer.commands.CreateAccountCommand;
import io.vertx.criptobuyer.models.PersistedAccount;
import io.vertx.criptobuyer.models.PersistedAccountKey;
import io.vertx.criptobuyer.models.PersistedAccountQuery;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.skeleton.httprouter.Constants;
import io.vertx.skeleton.httprouter.VertxHttpRoute;
import io.vertx.skeleton.orm.Repository;

public class AccountRoute implements VertxHttpRoute {

  private final AccountService accountService;

  public AccountRoute(
    AccountService accountService
  ) {
    this.accountService = accountService;
  }

  @Override
  public void registerRoutes(Router router) {
    router.post("/account").produces(Constants.APPLICATION_JSON).consumes(Constants.APPLICATION_JSON)
      .handler(
        routingContext -> {
          final var command = extractRequestObject(CreateAccountCommand.class, routingContext);
          accountService.createAccount(command)
            .subscribe()
            .with(
              avoid -> noContent(routingContext),
              routingContext::fail
            );
        }
      );
  }
}
