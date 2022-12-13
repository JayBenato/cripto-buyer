package io.vertx.criptobuyer.errors;

public class InsufficientBalanceException extends IllegalStateException {
  public InsufficientBalanceException(String s) {
    super(s);
  }
}
