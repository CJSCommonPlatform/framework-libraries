
package uk.gov.justice.api.resource;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("users/{userId}")
public interface UsersUserIdResource {


    @POST
    @Consumes("application/vnd.create-user+json")
    Response postVndCreateUserJsonUsersByUserId(
        @PathParam("userId")
        String userId, JsonObject entity);

    @POST
    @Consumes("application/vnd.update-user+json")
    Response postVndUpdateUserJsonUsersByUserId(
        @PathParam("userId")
        String userId, JsonObject entity);

}
