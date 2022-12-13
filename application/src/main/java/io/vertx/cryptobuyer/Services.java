package io.vertx.cryptobuyer;

import io.activej.inject.annotation.Inject;
import io.activej.inject.annotation.Provides;
import io.vertx.cryptobuyer.handlers.DefaultAccountService;
import io.vertx.cryptobuyer.handlers.DefaultOrderService;
import io.vertx.cryptobuyer.interfaces.AccountService;
import io.vertx.cryptobuyer.interfaces.OrderService;
import io.vertx.cryptobuyer.repository.models.*;
import io.vertx.skeleton.orm.Repository;
import io.vertx.skeleton.utils.VertxComponent;

public class Services extends VertxComponent {

  @Provides
  @Inject
  AccountService accountService(
    Repository<PersistedAccountKey, PersistedAccount, PersistedAccountQuery> account
  ) {
    return new DefaultAccountService(account);
  }

  @Provides
  @Inject
  OrderService orderService(
    Repository<PersistedAccountKey, PersistedAccount, PersistedAccountQuery> accounts,
    Repository<PersistedOrderKey, PersistedOrder, PersistedOrderQuery> orders
  ) {
    return new DefaultOrderService(accounts, orders);
  }
}
