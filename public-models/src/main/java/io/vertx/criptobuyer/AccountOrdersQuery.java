package io.vertx.criptobuyer;

import javax.ws.rs.QueryParam;
import java.util.List;

public record AccountOrdersQuery(
  @QueryParam("orderId") List<String> orderId,
  @QueryParam("accountId") List<String> accountId,
  @QueryParam("limitFrom") Long limitFrom,
  @QueryParam("limitTo") Long limitTo
) {
}
