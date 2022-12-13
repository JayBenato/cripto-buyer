package io.vertx.criptobuyer.mappers;

import io.smallrye.mutiny.tuples.Tuple2;
import io.vertx.criptobuyer.objects.OrderState;
import io.vertx.criptobuyer.models.PersistedOrder;
import io.vertx.criptobuyer.models.PersistedOrderKey;
import io.vertx.criptobuyer.models.PersistedOrderQuery;
import io.vertx.mutiny.sqlclient.templates.RowMapper;
import io.vertx.mutiny.sqlclient.templates.TupleMapper;
import io.vertx.skeleton.orm.RepositoryMapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrderMapper implements RepositoryMapper<PersistedOrderKey, PersistedOrder, PersistedOrderQuery> {

  private OrderMapper() {
  }

  public static final OrderMapper INSTANCE = new OrderMapper();

  public static final TupleMapper<PersistedOrder> TUPLE_MAPPER = TupleMapper.mapper(
    order -> {
      Map<String, Object> parameters = order.persistedRecord().params();
      parameters.put("account_id", order.accountId());
      parameters.put("order_id", order.orderId());
      parameters.put("amount", order.amount());
      parameters.put("state", order.state().name());
      parameters.put("price_limit", order.priceLimit());
      return parameters;
    }
  );

  public static final TupleMapper<PersistedOrderKey> KEY_TUPLE_MAPPER = TupleMapper.mapper(
    order -> Map.of("order_id", order.orderId(), "account_id", order.accountId())
  );
  public static final Class<PersistedOrderKey> PERSISTED_ORDER_KEY_CLASS = PersistedOrderKey.class;
  public static final Class<PersistedOrder> PERSISTED_ORDER_CLASS = PersistedOrder.class;
  public static final Set<String> KEY_COLUMNS = Set.of("order_id", "account_id");
  public static final Set<String> UPDATE_COLUMNS = Set.of("state");
  public static final Set<String> INSERT_COLUMNS = Set.of("state", "account_id", "order_id", "amount", "price_limit");
  public static final String TABLE = "orders";

  @Override
  public String table() {
    return TABLE;
  }

  @Override
  public Set<String> insertColumns() {
    return INSERT_COLUMNS;
  }

  @Override
  public Set<String> updateColumns() {
    return UPDATE_COLUMNS;
  }

  @Override
  public Set<String> keyColumns() {
    return KEY_COLUMNS;
  }

  @Override
  public RowMapper<PersistedOrder> rowMapper() {
    return RowMapper.newInstance(
      row -> new PersistedOrder(
        row.getString("order_id"),
        row.getString("account_id"),
        row.getNumeric("price_limit").bigDecimalValue(),
        row.getInteger("amount"),
        OrderState.valueOf(row.getString("state")),
        from(row)
      )
    );
  }

  @Override
  public TupleMapper<PersistedOrder> tupleMapper() {
    return TUPLE_MAPPER;
  }

  @Override
  public TupleMapper<PersistedOrderKey> keyMapper() {
    return KEY_TUPLE_MAPPER;
  }

  @Override
  public Class<PersistedOrder> valueClass() {
    return PERSISTED_ORDER_CLASS;
  }

  @Override
  public Class<PersistedOrderKey> keyClass() {
    return PERSISTED_ORDER_KEY_CLASS;
  }

  @Override
  public List<Tuple2<String, List<?>>> queryFieldsColumn(PersistedOrderQuery queryFilter) {
    return List.of(
      Tuple2.of("order_id", queryFilter.orderId()),
      Tuple2.of("account_id", queryFilter.accountId())
    );
  }
}
