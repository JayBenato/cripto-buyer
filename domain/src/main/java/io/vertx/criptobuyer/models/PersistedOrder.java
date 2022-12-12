package io.vertx.criptobuyer.models;

import io.vertx.criptobuyer.Order;
import io.vertx.criptobuyer.OrderState;
import io.vertx.skeleton.models.PersistedRecord;
import io.vertx.skeleton.models.RepositoryRecord;

import java.math.BigDecimal;

public record PersistedOrder(
  String orderId,
  String accountId,
  BigDecimal priceLimit,
  BigDecimal amount,
  OrderState state,
  PersistedRecord persistedRecord

) implements RepositoryRecord<PersistedOrder> {
  @Override
  public PersistedOrder with(PersistedRecord persistedRecord) {
    return new PersistedOrder(orderId, accountId, priceLimit, amount, state, persistedRecord);
  }

  public Order map() {
    return new Order(orderId, accountId, priceLimit, amount, state);
  }
}
