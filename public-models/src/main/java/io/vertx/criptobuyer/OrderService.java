package io.vertx.criptobuyer;

import io.smallrye.mutiny.Uni;
import io.vertx.skeleton.models.PublicQueryOptions;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@OpenAPIDefinition(
  info = @Info(
    title = "Order Service",
    version = "1.0.0-SNAPSHOT"
  )
)
@Path("/order")
public interface OrderService {


  @POST
  @Operation(operationId = "create-order")
  @APIResponse(responseCode = "201")
  @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = Error.class)))
  @APIResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = Error.class)))
  Uni<Void> createOrder(CreateOrderCommand command);

  @GET
  @Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
  @Operation(operationId = "query-order")
  @APIResponse(responseCode = "201")
  @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = Error.class)))
  @APIResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = Error.class)))
  Uni<List<Order>> fetchOrder(AccountOrdersQuery query, PublicQueryOptions queryOptions);

}
