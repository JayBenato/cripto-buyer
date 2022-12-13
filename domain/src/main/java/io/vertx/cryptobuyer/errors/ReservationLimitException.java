package io.vertx.cryptobuyer.errors;

public class ReservationLimitException extends IllegalStateException{
  public ReservationLimitException(String s) {
    super(s);
  }
}
