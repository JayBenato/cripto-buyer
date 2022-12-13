package io.vertx.criptobuyer.queries;

import java.util.List;

public record AccountOrdersQuery(
  List<String> orderId,
  List<String> accountId,
  Long limitFrom,
  Long limitTo
) {
}
