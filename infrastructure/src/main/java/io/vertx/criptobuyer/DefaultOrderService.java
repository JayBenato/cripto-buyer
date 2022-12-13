package io.vertx.criptobuyer;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import io.vertx.criptobuyer.commands.CreateOrderCommand;
import io.vertx.criptobuyer.entities.Order;
import io.vertx.criptobuyer.interfaces.OrderService;
import io.vertx.criptobuyer.models.*;
import io.vertx.criptobuyer.queries.AccountOrdersQuery;
import io.vertx.skeleton.models.PublicQueryOptions;
import io.vertx.skeleton.models.QueryOptions;
import io.vertx.skeleton.models.Tenant;
import io.vertx.skeleton.orm.Repository;

import java.util.List;


public class DefaultOrderService implements OrderService {

  private final Repository<PersistedAccountKey, PersistedAccount, PersistedAccountQuery> accounts;
  private final Repository<PersistedOrderKey, PersistedOrder, PersistedOrderQuery> orders;

  public DefaultOrderService(
    Repository<PersistedAccountKey, PersistedAccount, PersistedAccountQuery> accounts,
    Repository<PersistedOrderKey, PersistedOrder, PersistedOrderQuery> orders
  ) {
    this.accounts = accounts;
    this.orders = orders;
  }

  @Override
  public Uni<Order> createOrder(CreateOrderCommand command) {
    // if you are wondering how this would deal with concurrent updates the answer is optimistic locking.
    return accounts.selectByKey(new PersistedAccountKey(command.accountId()))
      .flatMap(Unchecked.function(account -> {
            final var order = Order.create(command);
            final var newState = account.map().reserve(order);
            return accounts.transaction(
              sqlConnection -> accounts.updateByKey(account.update(newState), sqlConnection)
                .flatMap(avoid -> orders.insert(PersistedOrder.from(order), sqlConnection))
            );
          }
        )
      )
      .map(PersistedOrder::map);
  }

  @Override
  public Uni<List<Order>> fetchOrder(AccountOrdersQuery query, PublicQueryOptions queryOptions) {
    return orders.query(new PersistedOrderQuery(
        query.orderId(),
        query.accountId(),
        query.limitFrom(),
        query.limitTo(),
        new QueryOptions(
          null,
          null,
          queryOptions.creationDateFrom(),
          queryOptions.creationDateTo(),
          queryOptions.lastUpdateFrom(),
          queryOptions.lastUpdateTo(),
          queryOptions.pageNumber(),
          queryOptions.pageSize(),
          null,
          Tenant.unknown()
        )
      )
    ).map(persistedOrders -> persistedOrders.stream().map(PersistedOrder::map).toList());
  }

}
