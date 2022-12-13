package io.vertx.cryptobuyer.interfaces;

import io.smallrye.mutiny.Uni;

import java.math.BigDecimal;

public interface PriceProvider {

  Uni<BigDecimal> currentPrice();


}
