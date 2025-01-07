package io.github.tayluansantos.expense_tracker_api.dto.category;

import jakarta.validation.constraints.NotNull;

public record CategoryRequestDto(@NotNull String name, String description) {
}
