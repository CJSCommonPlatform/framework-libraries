
package uk.gov.justice.api.resource;

import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import uk.gov.justice.raml.jaxrs.lib.Dispatcher;
import uk.gov.justice.raml.jaxrs.lib.RestProcessor;
import uk.gov.justice.raml.jaxrs.lib.UnmodifiableMapBuilder;

@Stateless
public class DefaultUsersUserIdResource
    implements UsersUserIdResource
{

    @Inject
    Dispatcher dispatcher;
    @Inject
    RestProcessor restProcessor;
    @Context
    HttpHeaders headers;

    @Override
    public Response postVndCreateUserJsonUsersByUserId(String userId, JsonObject entity) {
        {
            Map<String, String> pathParams = new UnmodifiableMapBuilder<String, String>().with("userId", userId).build();
            return restProcessor.process(dispatcher::dispatch, entity, headers, pathParams);
        }
    }

    @Override
    public Response postVndUpdateUserJsonUsersByUserId(String userId, JsonObject entity) {
        {
            Map<String, String> pathParams = new UnmodifiableMapBuilder<String, String>().with("userId", userId).build();
            return restProcessor.process(dispatcher::dispatch, entity, headers, pathParams);
        }
    }

}
