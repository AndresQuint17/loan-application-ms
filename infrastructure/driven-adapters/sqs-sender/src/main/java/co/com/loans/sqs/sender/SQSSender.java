package co.com.loans.sqs.sender;

import co.com.loans.model.loanapplication.dto.LoanValidationRequest;
import co.com.loans.model.loanapplication.gateways.MessageGateway;
import co.com.loans.sqs.sender.config.SQSSenderProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements MessageGateway {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public Mono<String> send(String email) {

        Map<String, String> messagePayload = new HashMap<>();
        messagePayload.put("solicitanteEmail", email);
        messagePayload.put("decision", "APPROVED");

        String jsonMessage = objectMapper.writeValueAsString(messagePayload);

        return Mono.fromCallable(() -> buildRequest(jsonMessage, properties.queueUrl()))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .map(SendMessageResponse::messageId);
    }

    @Override
    public Mono<String> sendMessageToLambdaCalculateCapacity(LoanValidationRequest bodyRequest) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(bodyRequest)) // Serializa a JSON
                .map(json -> buildRequest(json, properties.calculateCapacityQueueUrl())) // Construye el request SQS
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request))) // Envía el mensaje
                .doOnNext(response -> log.debug("Message sent to calculate capacity queue. MessageId: {}", response.messageId()))
                .map(SendMessageResponse::messageId);
    }

    private SendMessageRequest buildRequest(String message, String queueUrl) {
        return SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message)
                .build();
    }
}
