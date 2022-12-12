package io.vertx.criptobuyer.routes;

import io.vertx.criptobuyer.Account;
import io.vertx.criptobuyer.AccountService;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.skeleton.httprouter.Constants;
import io.vertx.skeleton.httprouter.VertxHttpRoute;

public class AccountRoute implements VertxHttpRoute {

  private final AccountService accountService;

  public AccountRoute(AccountService accountService) {
    this.accountService = accountService;
  }

  @Override
  public void registerRoutes(Router router) {
    router.post("/account").produces(Constants.APPLICATION_JSON).consumes(Constants.APPLICATION_JSON)
      .handler(
        routingContext -> {
          final var account = extractRequestObject(Account.class, routingContext);
          accountService.createAccount(account)
            .subscribe()
            .with(
              avoid -> noContent(routingContext),
              routingContext::fail
            );
        }
      );
  }
}
