package io.github.tayluansantos.expense_tracker_api.service;

import io.github.tayluansantos.expense_tracker_api.dto.user.UserRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.user.UserResponseDto;
import io.github.tayluansantos.expense_tracker_api.model.User;

import java.util.List;

public interface IUserService {
    UserResponseDto save(UserRequestDto user);
    User update(User user, Long id);
    UserResponseDto findById(Long id);
    List<UserResponseDto> findAll();
    void delete(Long id);
}
