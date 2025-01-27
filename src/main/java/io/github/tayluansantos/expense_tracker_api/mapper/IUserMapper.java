package io.github.tayluansantos.expense_tracker_api.mapper;

import io.github.tayluansantos.expense_tracker_api.dto.user.UserRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.user.UserResponseDto;
import io.github.tayluansantos.expense_tracker_api.model.UserModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",uses = {IExpenseMapper.class,ICategoryMapper.class})
public interface IUserMapper {

    UserResponseDto userToUserDto(UserModel userModel);

    UserModel userDtoToUser(UserRequestDto userRequestDto);

    List<UserResponseDto> convertListEntityToListDto(Iterable<UserModel> expenses);
}
