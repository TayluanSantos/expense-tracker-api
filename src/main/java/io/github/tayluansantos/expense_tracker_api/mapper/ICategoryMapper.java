package io.github.tayluansantos.expense_tracker_api.mapper;

import io.github.tayluansantos.expense_tracker_api.dto.category.CategoryRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.category.CategoryResponseDto;
import io.github.tayluansantos.expense_tracker_api.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICategoryMapper {

    CategoryResponseDto convertToDto(Category category);

    @Mapping(target = "id",ignore = true)
    Category convertToEntity(CategoryRequestDto categoryRequest);

    List<CategoryResponseDto> convertListEntityToListDto(Iterable<Category> categories);
}
