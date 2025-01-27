package io.github.tayluansantos.expense_tracker_api.service;

import io.github.tayluansantos.expense_tracker_api.dto.user.UserRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.user.UserResponseDto;

import java.util.List;

public interface IUserService {
    UserResponseDto save(UserRequestDto user);
    UserResponseDto update(UserRequestDto userRequestDto, Long id);
    UserResponseDto findById(Long id);
    List<UserResponseDto> findAll();
    void delete(Long id);
}
