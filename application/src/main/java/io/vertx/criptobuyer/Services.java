package io.vertx.criptobuyer;

import io.activej.inject.annotation.Inject;
import io.activej.inject.annotation.Provides;
import io.vertx.criptobuyer.handlers.AccountServiceImpl;
import io.vertx.criptobuyer.handlers.OrderServiceImpl;
import io.vertx.criptobuyer.models.*;
import io.vertx.skeleton.orm.Repository;
import io.vertx.skeleton.utils.VertxComponent;

public class Services extends VertxComponent {

  @Provides
  @Inject
  AccountService accountService(
    Repository<PersistedAccountKey, PersistedAccount, PersistedAccountQuery> account
  ) {
    return new AccountServiceImpl(account);
  }

  @Provides
  @Inject
  OrderService orderService(
    Repository<PersistedAccountKey, PersistedAccount, PersistedAccountQuery> accounts,
    Repository<PersistedOrderKey, PersistedOrder, PersistedOrderQuery> orders
  ) {
    return new OrderServiceImpl(accounts, orders);
  }
}
