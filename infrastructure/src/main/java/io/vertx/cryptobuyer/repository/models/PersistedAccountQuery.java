package io.vertx.cryptobuyer.repository.models;

import io.vertx.skeleton.models.Query;
import io.vertx.skeleton.models.QueryOptions;

import java.util.List;

public record PersistedAccountQuery(
  List<String> accountIds,
  QueryOptions options
) implements Query {
}
