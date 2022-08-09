package me.sonam.email;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

/**
 * Set Email route
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "Email rest service Swagger doc", version = "1.0", description = "Documentation APIs v1.0"))
public class EmailRouter {
    private static final Logger LOG = LoggerFactory.getLogger(EmailRouter.class);

    @Bean
    @RouterOperations(
            {
                    @RouterOperation(path = "/email"
                            , produces = {
                            MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET,
                            operation = @Operation(operationId = "email", responses = {
                                    @ApiResponse(responseCode = "200", description = "successful operation"),
                                    @ApiResponse(responseCode = "400", description = "invalid user id")}
                            ))
            }
    )
    public RouterFunction<ServerResponse> route(EmailHandler emailHandler) {
        LOG.info("building email router function");
        return RouterFunctions.route(POST("/email").and(accept(MediaType.APPLICATION_JSON)),
                emailHandler::email);
    }
}
