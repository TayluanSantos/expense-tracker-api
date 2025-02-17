package io.github.tayluansantos.expense_tracker_api.dto.user;

import io.github.tayluansantos.expense_tracker_api.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequestDto(@NotBlank String name,
                             @NotBlank @Email String email,
                             @NotBlank @Size(min = 8) String password,
                             @NotNull Role role)

{ }
