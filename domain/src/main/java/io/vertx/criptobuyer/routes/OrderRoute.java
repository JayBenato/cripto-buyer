package io.vertx.criptobuyer.routes;

import io.vertx.core.json.JsonArray;
import io.vertx.criptobuyer.AccountOrdersQuery;
import io.vertx.criptobuyer.CreateOrderCommand;
import io.vertx.criptobuyer.OrderService;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.skeleton.httprouter.Constants;
import io.vertx.skeleton.httprouter.VertxHttpRoute;

public class OrderRoute implements VertxHttpRoute {

  private final OrderService orderService;

  public OrderRoute(OrderService orderService) {
    this.orderService = orderService;
  }

  @Override
  public void registerRoutes(Router router) {

    router.post("/order").consumes(Constants.APPLICATION_JSON).produces(Constants.APPLICATION_JSON)
      .handler(
        routingContext -> orderService.createOrder(extractRequestObject(CreateOrderCommand.class, routingContext))
          .subscribe()
          .with(
            avoid -> noContent(routingContext),
            routingContext::fail
          )
      );

    router.get("/order").produces(Constants.APPLICATION_JSON)
      .handler(routingContext -> {
          final var query = new AccountOrdersQuery(
            routingContext.queryParam("orderId"),
            routingContext.queryParam("accountId"),
            routingContext.queryParam("limitFrom").stream().findFirst().map(Long::valueOf).orElse(null),
            routingContext.queryParam("limitTo").stream().findFirst().map(Long::valueOf).orElse(null)
          );
          final var options = getQueryOptions(routingContext);
          orderService.fetchOrder(query, options)
            .subscribe()
            .with(
              orders -> okWithArrayBody(routingContext, new JsonArray(orders)),
              routingContext::fail
            );
        }
      );
  }

}
