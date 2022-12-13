package io.vertx.cryptobuyer;


import io.activej.inject.annotation.Inject;
import io.activej.inject.annotation.Provides;
import io.vertx.cryptobuyer.interfaces.AccountService;
import io.vertx.cryptobuyer.interfaces.OrderService;
import io.vertx.cryptobuyer.routes.*;
import io.vertx.skeleton.utils.VertxComponent;

public class Routes extends VertxComponent {


  @Provides
  @Inject
  AccountRoute accountRoute(
    AccountService accountService
  ) {
    return new AccountRoute(accountService);
  }

  @Provides
  @Inject
  OrderRoute orderService(OrderService orderService) {
    return new OrderRoute(orderService);
  }

}
