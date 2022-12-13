package io.vertx.criptobuyer.mappers;

import io.smallrye.mutiny.tuples.Tuple2;
import io.vertx.criptobuyer.models.PersistedAccount;
import io.vertx.criptobuyer.models.PersistedAccountKey;
import io.vertx.criptobuyer.models.PersistedAccountQuery;
import io.vertx.mutiny.sqlclient.templates.RowMapper;
import io.vertx.mutiny.sqlclient.templates.TupleMapper;
import io.vertx.skeleton.orm.RepositoryMapper;
import io.vertx.sqlclient.data.Numeric;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class AccountMapper implements RepositoryMapper<PersistedAccountKey, PersistedAccount, PersistedAccountQuery> {

  private AccountMapper() {
  }

  public static final AccountMapper INSTANCE = new AccountMapper();

  public static final TupleMapper<PersistedAccount> TUPLE_MAPPER = TupleMapper.mapper(
    order -> {
      Map<String, Object> parameters = order.persistedRecord().params();
      parameters.put("account_id", order.accountId());
      parameters.put("balance",order.balance());
      parameters.put("reserved_amount", order.reservedAmount());
      return parameters;
    }
  );
  public static final TupleMapper<PersistedAccountKey> KEY_TUPLE_MAPPER = TupleMapper.mapper(
    accountKey -> Map.of("account_id", accountKey.accountId())
  );
  public static final Class<PersistedAccountKey> PERSISTED_ORDER_KEY_CLASS = PersistedAccountKey.class;
  public static final Class<PersistedAccount> PERSISTED_ORDER_CLASS = PersistedAccount.class;
  public static final Set<String> KEY_COLUMNS = Set.of("account_id");
  public static final Set<String> UPDATE_COLUMNS = Set.of("balance");
  public static final Set<String> INSERT_COLUMNS = Set.of("balance", "reserved_amount", "account_id");
  public static final String TABLE = "accounts";

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
  public RowMapper<PersistedAccount> rowMapper() {
    return RowMapper.newInstance(
      row -> new PersistedAccount(
        row.getString("account_id"),
        row.getNumeric("balance").bigDecimalValue(),
        row.getNumeric("reserved_amount").bigDecimalValue(),
        from(row)
      )
    );
  }

  @Override
  public TupleMapper<PersistedAccount> tupleMapper() {
    return TUPLE_MAPPER;
  }

  @Override
  public TupleMapper<PersistedAccountKey> keyMapper() {
    return KEY_TUPLE_MAPPER;
  }

  @Override
  public Class<PersistedAccount> valueClass() {
    return PERSISTED_ORDER_CLASS;
  }

  @Override
  public Class<PersistedAccountKey> keyClass() {
    return PERSISTED_ORDER_KEY_CLASS;
  }

  @Override
  public List<Tuple2<String, List<?>>> queryFieldsColumn(PersistedAccountQuery queryFilter) {
    return List.of(Tuple2.of("account_id", queryFilter.accountIds()));
  }
}
