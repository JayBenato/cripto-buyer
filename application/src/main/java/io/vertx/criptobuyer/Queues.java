package io.vertx.criptobuyer;

import io.activej.inject.annotation.Inject;
import io.activej.inject.annotation.Provides;
import io.vertx.criptobuyer.commands.ProcessOrderCommand;
import io.vertx.criptobuyer.entities.Order;
import io.vertx.criptobuyer.interfaces.OrderExchange;
import io.vertx.criptobuyer.models.*;
import io.vertx.skeleton.orm.Repository;
import io.vertx.skeleton.utils.VertxComponent;

public class Queues extends VertxComponent {

  @Provides
  @Inject
  OrderProcessor orderProcessor(
    final Repository<PersistedOrderKey, PersistedOrder, PersistedOrderQuery> orders,
    final Repository<PersistedAccountKey, PersistedAccount, PersistedAccountQuery> accounts
  ) {
    return new OrderProcessor(
      orders,
      accounts,
      new OrderExchange() {
        @Override
        public Order process(ProcessOrderCommand order) {
          return OrderExchange.super.process(order);
        }
      }
    );
  }
}
