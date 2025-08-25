package co.com.loans.api;

import co.com.loans.api.model.LoanApplicationRequest;
import co.com.loans.api.model.LoanApplicationResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.extern.log4j.Log4j2;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.HEAD;
import static org.springframework.web.reactive.function.server.RequestPredicates.headers;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


import java.util.List;
import java.util.Map;

@Log4j2
@AllArgsConstructor
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Crediya Loan Application MS",
                version = "1.0.0",
                description = "API for submitting and managing loan applications to Crediya."
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local development server")
        }
)
public class LoanApplicationsApiRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/solicitud",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = LoanApplicationsApiHandler.class,
                    beanMethod = "submitLoanApplication",
                    operation = @Operation(
                            operationId = "submitLoanApplication",
                            summary = "Submit a new loan application",
                            tags = {"Loan Applications"},
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = LoanApplicationRequest.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Loan application created successfully",
                                            content = @Content(
                                                    schema = @Schema(implementation = LoanApplicationResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid request payload"
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunctionLoanApplicationsApi(LoanApplicationsApiHandler handler) {
        return route(POST("/api/v1/solicitud"), handler::submitLoanApplication);
    }

}
