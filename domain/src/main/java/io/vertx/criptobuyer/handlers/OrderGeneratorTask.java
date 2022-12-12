package io.vertx.criptobuyer.handlers;

import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import io.vertx.criptobuyer.Account;
import io.vertx.criptobuyer.CreateOrderCommand;
import io.vertx.mutiny.ext.web.client.WebClient;
import io.vertx.skeleton.task.SynchronizationStrategy;
import io.vertx.skeleton.task.SynchronizedTask;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class OrderGeneratorTask implements SynchronizedTask {

  private final WebClient client;

  public OrderGeneratorTask(WebClient client) {
    this.client = client;
  }

  @Override
  public Uni<Void> performTask() {
    final var account = getAccount();
    return client.post(8080, "localhost", "account").sendJson(JsonObject.mapFrom(account))
      .flatMap(avoid -> client.post(8080, "localhost", "order").sendJson(JsonObject.mapFrom(getOrder(account))))
      .replaceWithVoid();
  }

  private Account getAccount() {
    return new Account(
      UUID.randomUUID().toString(),
      randomBigDecimal(new BigDecimal(10000))
    );
  }

  private CreateOrderCommand getOrder(Account account) {
    return new CreateOrderCommand(
      account.accountId(),
      randomBigDecimal(account.balance()),
      randomBigDecimal(account.balance())
    );
  }

  private BigDecimal randomBigDecimal(BigDecimal max) {
    BigDecimal randFromDouble = BigDecimal.valueOf(Math.random());
    return randFromDouble.divide(max, RoundingMode.DOWN);
  }

  @Override
  public SynchronizationStrategy strategy() {
    return SynchronizationStrategy.LOCAL;
  }
}
