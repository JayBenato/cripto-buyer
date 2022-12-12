package io.vertx.criptobuyer;


import java.math.BigDecimal;

public record Account(
  String accountId,
  BigDecimal balance
){
}
