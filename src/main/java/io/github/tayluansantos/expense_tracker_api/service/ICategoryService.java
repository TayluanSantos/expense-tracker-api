package io.github.tayluansantos.expense_tracker_api.service;

import io.github.tayluansantos.expense_tracker_api.dto.category.CategoryRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.category.CategoryResponseDto;

import java.util.List;

public interface ICategoryService {
    List<CategoryResponseDto> findAll();
    CategoryResponseDto findById(Long id);
    CategoryResponseDto save(CategoryRequestDto categoryRequestDto);
    CategoryResponseDto update(CategoryRequestDto categoryRequestDto, Long id);
    void delete(Long id);
}
