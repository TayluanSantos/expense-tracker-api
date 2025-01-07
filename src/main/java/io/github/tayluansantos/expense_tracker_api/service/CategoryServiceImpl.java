package io.github.tayluansantos.expense_tracker_api.service;

import io.github.tayluansantos.expense_tracker_api.dto.category.CategoryRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.category.CategoryResponseDto;
import io.github.tayluansantos.expense_tracker_api.dto.user.UserRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.user.UserResponseDto;
import io.github.tayluansantos.expense_tracker_api.exception.ResourceNotFoundException;
import io.github.tayluansantos.expense_tracker_api.mapper.ICategoryMapper;
import io.github.tayluansantos.expense_tracker_api.mapper.IUserMapper;
import io.github.tayluansantos.expense_tracker_api.model.Category;
import io.github.tayluansantos.expense_tracker_api.model.User;
import io.github.tayluansantos.expense_tracker_api.repository.CategoryRepository;
import io.github.tayluansantos.expense_tracker_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ICategoryMapper categoryMapper;


    @Override
    public List<CategoryResponseDto> findAll() {
        return categoryMapper.convertListEntityToListDto(categoryRepository.findAll());
    }

    @Override
    public CategoryResponseDto findById(Long id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found category with id" + id));
        return categoryMapper.convertToDto(category);
    }

    @Override
    public CategoryResponseDto save(Long id, CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository
                .save(categoryMapper.convertToEntity(categoryRequestDto));

        return categoryMapper.convertToDto(category);
    }

    @Override
    public CategoryResponseDto update(CategoryRequestDto categoryRequestDto, Long id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found category with id" + id));
        category.setName(categoryRequestDto.name());
        category.setDescription(categoryRequestDto.description());

        return categoryMapper.convertToDto(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found category with id" + id));
        categoryRepository.delete(category);
    }
}
