package io.vertx.criptobuyer.models;


public record PersistedOrderKey(
  String orderId,
  String accountId
) {
}
