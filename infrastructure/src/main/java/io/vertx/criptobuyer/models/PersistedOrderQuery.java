package io.vertx.criptobuyer.models;

import io.vertx.skeleton.models.Query;
import io.vertx.skeleton.models.QueryOptions;

import java.util.List;

public record PersistedOrderQuery(
  List<String> orderId,
  List<String> accountId,
  Long limitFrom,
  Long limitTo,
  QueryOptions options
) implements Query {
}
