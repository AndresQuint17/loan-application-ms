package co.com.loans.sqs.listener;

import co.com.loans.model.loanapplication.dto.LoanValidationResponse;
import co.com.loans.usecase.changeLoanApplicationStatus.ChangeLoanApplicationStatusUseCase;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {
    private final ChangeLoanApplicationStatusUseCase changeLoanApplicationStatusUseCase;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Mono<Void> apply(Message message) {
        LoanValidationResponse validationResponse = objectMapper.readValue(message.body(), LoanValidationResponse.class);
        return changeLoanApplicationStatusUseCase.updateStatus(validationResponse);
    }
}
