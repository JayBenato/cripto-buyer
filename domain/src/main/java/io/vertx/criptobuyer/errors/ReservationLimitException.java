package io.vertx.criptobuyer.errors;

public class ReservationLimitException extends IllegalStateException{
  public ReservationLimitException(String s) {
    super(s);
  }
}
