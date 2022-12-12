package io.vertx.criptobuyer;

import io.smallrye.mutiny.Uni;
import io.vertx.skeleton.models.PublicQueryOptions;

import java.util.List;

public class MockImpl implements AccountService,OrderService{
  @Override
  public Uni<Void> createAccount(Account account) {
    return null;
  }

  @Override
  public Uni<Void> createOrder(CreateOrderCommand command) {
    return null;
  }

  @Override
  public Uni<List<Order>> fetchOrder(AccountOrdersQuery query, PublicQueryOptions queryOptions) {
    return null;
  }
}
