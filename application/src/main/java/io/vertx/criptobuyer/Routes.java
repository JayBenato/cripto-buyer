package io.vertx.criptobuyer;

import io.activej.inject.annotation.Inject;
import io.activej.inject.annotation.Provides;
import io.vertx.criptobuyer.routes.AccountRoute;
import io.vertx.criptobuyer.routes.OrderRoute;
import io.vertx.skeleton.utils.VertxComponent;

public class Routes extends VertxComponent {


  @Provides
  @Inject
  AccountRoute accountRoute(AccountService accountService) {
    return new AccountRoute(accountService);
  }

  @Provides
  @Inject
  OrderRoute orderService(OrderService orderService) {
    return new OrderRoute(orderService);
  }

}
