package io.vertx.cryptobuyer.errors;

public class InsufficientBalanceException extends IllegalStateException {
  public InsufficientBalanceException(String s) {
    super(s);
  }
}
