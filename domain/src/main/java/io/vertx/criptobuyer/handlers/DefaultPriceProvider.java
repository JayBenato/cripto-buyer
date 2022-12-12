package io.vertx.criptobuyer.handlers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.smallrye.mutiny.Uni;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.criptobuyer.PriceProvider;
import io.vertx.mutiny.ext.web.client.WebClient;

import java.math.BigDecimal;

public class DefaultPriceProvider implements PriceProvider {


  private final WebClient webClient;
  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPriceProvider.class);

  public DefaultPriceProvider(WebClient webClient) {
    this.webClient = webClient;
  }

  @Override
  public Uni<BigDecimal> currentPrice() {
    return webClient.get(5000,"127.0.0.1","btc-price").send()
      .map(httpResponse -> {
        LOGGER.info(httpResponse.bodyAsString());
        return httpResponse.bodyAsJson(Price.class).price();
      });
  }



  @JsonIgnoreProperties(ignoreUnknown = true)
  private record Price(
    BigDecimal price
  ){}





}
