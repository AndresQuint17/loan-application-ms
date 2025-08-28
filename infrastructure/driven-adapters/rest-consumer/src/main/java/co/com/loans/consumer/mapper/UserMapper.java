package co.com.loans.consumer.mapper;

import co.com.loans.consumer.dto.ResponseUserByIdCardDto;
import co.com.loans.model.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    ResponseUserByIdCardDto toUserByIdResponseDto(User entity);
    User toDomain(ResponseUserByIdCardDto responseFindUserByIdCardDto);
}
