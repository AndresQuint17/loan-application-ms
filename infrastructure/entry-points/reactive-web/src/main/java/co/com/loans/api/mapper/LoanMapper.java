package co.com.loans.api.mapper;

import co.com.loans.api.model.LoanApplicationRequest;
import co.com.loans.api.model.LoanApplicationResponse;
import co.com.loans.model.loanapplication.LoanApplication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanMapper {
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "term", target = "term")
    @Mapping(source = "loanTypeId", target = "loanTypeId")
    @Mapping(target = "statusId", ignore = true) // Ignorar statusId
    @Mapping(target = "email", ignore = true) // Ignorar email
    LoanApplication toDomain(LoanApplicationRequest loanApplicationRequest);

    @Mapping(source = "statusId", target = "status")
    LoanApplicationResponse toLoanApplicationResponse(LoanApplication model);
}
