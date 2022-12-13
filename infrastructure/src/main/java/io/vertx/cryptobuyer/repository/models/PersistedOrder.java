package io.vertx.cryptobuyer.repository.models;

import io.vertx.cryptobuyer.entities.Order;
import io.vertx.cryptobuyer.objects.OrderState;
import io.vertx.skeleton.models.PersistedRecord;
import io.vertx.skeleton.models.RepositoryRecord;
import io.vertx.skeleton.models.Tenant;

import java.math.BigDecimal;

public record PersistedOrder(
  String orderId,
  String accountId,
  BigDecimal priceLimit,
  Integer amount,
  OrderState state,
  PersistedRecord persistedRecord

) implements RepositoryRecord<PersistedOrder> {
  public static PersistedOrder from(Order order) {
    return new PersistedOrder(order.orderId(),order.accountId(),order.priceLimit(),order.amount(), order.state(), PersistedRecord.newRecord(Tenant.unknown()));
  }

  @Override
  public PersistedOrder with(PersistedRecord persistedRecord) {
    return new PersistedOrder(orderId, accountId, priceLimit, amount, state, persistedRecord);
  }

  public Order map() {
    return new Order(orderId, accountId, priceLimit, amount, state);
  }
}
