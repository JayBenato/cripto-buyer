package io.vertx.criptobuyer.interfaces;

import io.smallrye.mutiny.Uni;
import io.vertx.criptobuyer.queries.AccountOrdersQuery;
import io.vertx.criptobuyer.commands.CreateOrderCommand;
import io.vertx.criptobuyer.entities.Order;
import io.vertx.skeleton.models.PublicQueryOptions;

import java.util.List;

public interface OrderService {


  Uni<Order> createOrder(CreateOrderCommand command);

  Uni<List<Order>> fetchOrder(AccountOrdersQuery query, PublicQueryOptions queryOptions);

}
