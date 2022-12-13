package io.vertx.cryptobuyer.interfaces;

import io.smallrye.mutiny.Uni;
import io.vertx.cryptobuyer.queries.AccountOrdersQuery;
import io.vertx.cryptobuyer.commands.CreateOrderCommand;
import io.vertx.cryptobuyer.entities.Order;
import io.vertx.skeleton.models.PublicQueryOptions;

import java.util.List;

public interface OrderService {


  Uni<Order> createOrder(CreateOrderCommand command);

  Uni<List<Order>> fetchOrder(AccountOrdersQuery query, PublicQueryOptions queryOptions);

}
