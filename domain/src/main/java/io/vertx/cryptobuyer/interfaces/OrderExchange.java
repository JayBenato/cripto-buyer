package io.vertx.cryptobuyer.interfaces;

import io.vertx.cryptobuyer.commands.ProcessOrderCommand;
import io.vertx.cryptobuyer.entities.Order;

public interface OrderExchange {


  default Order process(ProcessOrderCommand command) {
    // if this was real it would be bounded to a transaction where the first step would be transform order into btc
    // second part put btc in account
    return command.order().processed();
  }


}
