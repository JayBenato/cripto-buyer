package io.vertx.cryptobuyer.repository.models;


public record PersistedOrderKey(
  String orderId,
  String accountId
) {
}
