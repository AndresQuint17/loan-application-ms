package co.com.loans.api;

import co.com.loans.api.exception.GlobalErrorHandler;
import co.com.loans.api.model.LoanApplicationRequest;
import co.com.loans.api.model.LoanApplicationResponse;
import co.com.loans.api.model.PendingReviewLoanApplicationsResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.http.MediaType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

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
    private static final Logger logger = LoggerFactory.getLogger(LoanApplicationsApiRouter.class);

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
            ),
            @RouterOperation(
                    path = "/api/v1/solicitud",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = LoanApplicationsApiHandler.class,
                    beanMethod = "listLoanApplications",
                    operation = @Operation(
                            operationId = "listLoanApplications",
                            summary = "List pending loan applications",
                            tags = {"Loan Applications"},
                            parameters = {
                                    @Parameter(name = "page", description = "Page number for pagination", required = true, schema = @Schema(type = "integer", defaultValue = "0")),
                                    @Parameter(name = "size", description = "Number of items per page", required = true, schema = @Schema(type = "integer", defaultValue = "20"))
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "List of pending applications retrieved successfully",
                                            content = @Content(
                                                    schema = @Schema(implementation = PendingReviewLoanApplicationsResponse.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "404", description = "No loan applications found")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunctionLoanApplicationsApi(LoanApplicationsApiHandler handler, GlobalErrorHandler globalErrorHandler) {
        return route(POST("/api/v1/solicitud"), handler::submitLoanApplication)
                .andRoute(GET("/api/v1/solicitud"), handler::listLoanApplications)
                .filter((request, next) ->
                        next.handle(request)
                                .onErrorResume(error -> {
                                    logger.error(error.getMessage());
                                    return globalErrorHandler.handleError(error);
                                })
                );
    }
}
