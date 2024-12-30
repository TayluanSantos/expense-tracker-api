package io.github.tayluansantos.expense_tracker_api.mapper;

import io.github.tayluansantos.expense_tracker_api.dto.user.UserRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.user.UserResponseDto;
import io.github.tayluansantos.expense_tracker_api.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IUserMapper {

    UserResponseDto  userToUserDto(User user);
    User userDtoToUser(UserRequestDto user);

    List<UserResponseDto> convertListEntityToListDto(Iterable<User> users);

}
