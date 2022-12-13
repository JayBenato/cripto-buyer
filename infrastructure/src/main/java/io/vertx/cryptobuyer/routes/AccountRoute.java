package io.vertx.cryptobuyer.routes;

import io.vertx.cryptobuyer.interfaces.AccountService;
import io.vertx.cryptobuyer.commands.CreateAccountCommand;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.skeleton.httprouter.Constants;
import io.vertx.skeleton.httprouter.VertxHttpRoute;

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
