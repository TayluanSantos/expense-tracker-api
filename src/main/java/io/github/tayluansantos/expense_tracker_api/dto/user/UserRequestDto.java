package io.github.tayluansantos.expense_tracker_api.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequestDto(@NotNull String name,
                             @NotNull @Email String email,
                             @NotNull @Size(min = 8) String password) { }
