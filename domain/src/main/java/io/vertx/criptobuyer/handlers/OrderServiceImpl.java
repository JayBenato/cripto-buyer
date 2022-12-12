package io.vertx.criptobuyer.handlers;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import io.vertx.criptobuyer.*;
import io.vertx.criptobuyer.models.*;
import io.vertx.skeleton.models.PersistedRecord;
import io.vertx.skeleton.models.PublicQueryOptions;
import io.vertx.skeleton.models.QueryOptions;
import io.vertx.skeleton.models.Tenant;
import io.vertx.skeleton.orm.Repository;

import java.util.List;
import java.util.UUID;

public class OrderServiceImpl implements OrderService {

  private final Repository<PersistedAccountKey, PersistedAccount, PersistedAccountQuery> accounts;
  private final Repository<PersistedOrderKey, PersistedOrder, PersistedOrderQuery> orders;

  public OrderServiceImpl(
    Repository<PersistedAccountKey, PersistedAccount, PersistedAccountQuery> accounts,
    Repository<PersistedOrderKey, PersistedOrder, PersistedOrderQuery> orders
  ) {
    this.accounts = accounts;
    this.orders = orders;
  }

  @Override
  public Uni<Void> createOrder(CreateOrderCommand command) {
    // if you are wondering how this would deal with concurrent updates the answer is optimistic locking.
    return accounts.selectByKey(new PersistedAccountKey(command.accountId()))
      .flatMap(Unchecked.function(account -> {
            if (account.balance().compareTo(command.amount()) < 0) {
              throw new IllegalStateException("Insufficient balance !");
            } else {
              return accounts.transaction(
                sqlConnection -> accounts.updateByKey(account.deductFromBalance(command.amount()), sqlConnection)
                  .flatMap(avoid -> orders.insert(order(command), sqlConnection))
              );
            }
          }
        )
      )
      .replaceWithVoid();
  }

  private static PersistedOrder order(CreateOrderCommand command) {
    return new PersistedOrder(
      UUID.randomUUID().toString(),
      command.accountId(),
      command.priceLimit(),
      command.amount(),
      OrderState.CREATED,
      PersistedRecord.newRecord(Tenant.unknown())
    );
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
