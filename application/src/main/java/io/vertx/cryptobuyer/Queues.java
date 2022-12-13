package io.vertx.cryptobuyer;

import io.activej.inject.annotation.Inject;
import io.activej.inject.annotation.Provides;
import io.vertx.cryptobuyer.commands.ProcessOrderCommand;
import io.vertx.cryptobuyer.entities.Order;
import io.vertx.cryptobuyer.handlers.OrderProcessor;
import io.vertx.cryptobuyer.interfaces.OrderExchange;
import io.vertx.cryptobuyer.repository.models.*;
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
