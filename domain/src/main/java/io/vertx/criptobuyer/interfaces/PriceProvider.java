package io.vertx.criptobuyer.interfaces;

import io.smallrye.mutiny.Uni;

import java.math.BigDecimal;

public interface PriceProvider {

  Uni<BigDecimal> currentPrice();


}
