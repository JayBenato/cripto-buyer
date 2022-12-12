package io.vertx.criptobuyer;

import io.smallrye.mutiny.Uni;

import java.math.BigDecimal;

public interface PriceProvider {

  Uni<BigDecimal> currentPrice();


}
