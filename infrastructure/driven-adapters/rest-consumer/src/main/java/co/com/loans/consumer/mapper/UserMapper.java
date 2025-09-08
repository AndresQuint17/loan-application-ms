package co.com.loans.consumer.mapper;

import co.com.loans.consumer.dto.ResponseUserByIdCardDto;
import co.com.loans.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    ResponseUserByIdCardDto toUserByIdResponseDto(User entity);
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "dateOfBirth", ignore = true)
    @Mapping(target = "address", ignore = true)
    User toDomain(ResponseUserByIdCardDto responseFindUserByIdCardDto);
}
