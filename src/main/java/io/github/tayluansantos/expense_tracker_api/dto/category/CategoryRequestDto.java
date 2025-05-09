package io.github.tayluansantos.expense_tracker_api.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDto(@NotBlank String name, String description) {
}
