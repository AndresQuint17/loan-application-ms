package co.com.loans.model.loanapplication.gateways;

import co.com.loans.model.loanapplication.dto.LoanValidationRequest;
import co.com.loans.model.loanapplication.dto.LoanValidationResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface MessageGateway {
    Mono<String> sendNotification(String email, String newStatus);
    Mono<String> sendNotificationWithPaymentPlan(String email, LoanValidationResponse validationResponse);
    Mono<String> sendMessageToCalculateCapacity(LoanValidationRequest bodyRequest);
    Mono<String> updateReportApprovedApplications(BigDecimal amountApproved);
}
