package co.com.loans.consumer.dto;

import java.math.BigDecimal;

public record ResponseUserByIdCardDto(
        String firstName,
        String lastName,
        String idCard,
        String email,
        BigDecimal baseSalary
) {
}
