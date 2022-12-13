package io.vertx.criptobuyer.interfaces;

import io.vertx.criptobuyer.commands.ProcessOrderCommand;
import io.vertx.criptobuyer.entities.Order;

public interface OrderExchange {


  default Order process(ProcessOrderCommand command) {
    // if this was real it would be bounded to a transaction where the first step would be transform order into btc
    // second part put btc in account
    return command.order().processed();
  }


}
