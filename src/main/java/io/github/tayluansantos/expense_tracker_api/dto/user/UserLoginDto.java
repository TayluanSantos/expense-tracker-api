package io.github.tayluansantos.expense_tracker_api.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginDto(@NotBlank @Email String email,
                           @NotBlank @Size(min = 8) String password){ }
