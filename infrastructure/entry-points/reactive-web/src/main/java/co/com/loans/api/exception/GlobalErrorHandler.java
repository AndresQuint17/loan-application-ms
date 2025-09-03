package co.com.loans.api.exception;

import co.com.loans.model.exceptions.*;
import co.com.loans.usecase.registerLoanApplication.exceptions.UserDoesNotMatchError;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class GlobalErrorHandler {

    public Mono<ServerResponse> handleError(Throwable throwable) {
        final String BUSINESS_VALIDATION_ERROR = "Business validation error";
        final String APPLICATION_ERROR = "Application error";
        final String SERVER_ERROR = "Internal server error";
        final String EXTERNAL_SYSTEM_ERROR = "External system error";

        // Authorization denied access
        if(throwable instanceof AuthorizationDeniedException) {
            return ServerResponse.status(HttpStatus.FORBIDDEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "error", BUSINESS_VALIDATION_ERROR,
                            "message", "You do not have authorization to use this resource"
                    ));
        }
        // Bad token error
        if (throwable instanceof BadTokenError) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "error", BUSINESS_VALIDATION_ERROR,
                            "message", throwable.getMessage()
                    ));
        }
        // Data field error
        if (throwable instanceof DataFieldException) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "error", BUSINESS_VALIDATION_ERROR,
                            "status", ((DataFieldException) throwable).getStatus(),
                            "message", throwable.getMessage()
                    ));
        }
        // User does not exist
        if (throwable instanceof UserDoesNotExistException) {
            return ServerResponse.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "error", BUSINESS_VALIDATION_ERROR,
                            "status", ((UserDoesNotExistException) throwable).getStatus(),
                            "message", throwable.getMessage()
                    ));
        }
        // User does not match error
        if (throwable instanceof UserDoesNotMatchError) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "error", BUSINESS_VALIDATION_ERROR,
                            "message", throwable.getMessage()
                    ));
        }
        // Connection to the rest api failed
        if (throwable instanceof ConnectionFailedException) {
            return ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "error", EXTERNAL_SYSTEM_ERROR,
                            "status", ((ConnectionFailedException) throwable).getStatus(),
                            "message", throwable.getMessage()
                    ));
        }
        // Data source connection failed
        if(throwable instanceof DataAccessResourceFailureException) {
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "error", APPLICATION_ERROR,
                            "status", 500,
                            "message", "Error connecting to data source"
                    ));
        }
        // Insertion error in database
        if (throwable instanceof SavingInDatabaseException) {
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "error", BUSINESS_VALIDATION_ERROR,
                            "status", ((SavingInDatabaseException) throwable).getStatus(),
                            "message", throwable.getMessage()
                    ));
        }
        // Loan type does not exist
        if (throwable instanceof LoanTypeDoesNotExistException) {
            return ServerResponse.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "error", BUSINESS_VALIDATION_ERROR,
                            "status", ((LoanTypeDoesNotExistException) throwable).getStatus(),
                            "message", throwable.getMessage()
                    ));
        }
        // Malformed JWT
        if (throwable instanceof MalformedJwtException) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "error",BUSINESS_VALIDATION_ERROR,
                            "message", throwable.getMessage()
                    ));
        }
        // Unexpected error from rest client, driven adapter
        if (throwable instanceof UnexpectedException) {
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "error", SERVER_ERROR,
                            "status",((UnexpectedException) throwable).getStatus(),
                            "message", throwable.getMessage()
                    ));
        }
        // Generic exception
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "error", SERVER_ERROR,
                        "status",500,
                        "message", "Unexpected server error occurred"
                ));
    }
}
