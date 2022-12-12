package io.vertx.criptobuyer;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@OpenAPIDefinition(
  info = @Info(
    title = "Account Service",
    version = "1.0.0-SNAPSHOT"
  )
)
@Path("/account")
public interface AccountService {
  @POST
  @Operation(operationId = "create-account")
  @APIResponse(responseCode = "201")
  @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = Error.class)))
  @APIResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = Error.class)))
  Uni<Void> createAccount(Account account);

}
