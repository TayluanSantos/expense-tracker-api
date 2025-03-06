package io.github.tayluansantos.expense_tracker_api.service;

import io.github.tayluansantos.expense_tracker_api.dto.category.CategoryRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.category.CategoryResponseDto;
import io.github.tayluansantos.expense_tracker_api.exception.ResourceNotFoundException;
import io.github.tayluansantos.expense_tracker_api.mapper.ICategoryMapper;
import io.github.tayluansantos.expense_tracker_api.model.Category;
import io.github.tayluansantos.expense_tracker_api.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService{

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;
    private final ICategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               ICategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryResponseDto> findAll() {

        logger.info("Retrieving all categories");
        return categoryMapper.convertListEntityToListDto(categoryRepository.findAll());
    }

    @Override
    public CategoryResponseDto findById(Long id) {

        logger.info("Attempting to find a category by id");

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(()-> {
                    logger.warn("Failed to find a category with id:{}",id);
                    return new ResourceNotFoundException("Cannot found category with id:" + id);
                });
        CategoryResponseDto categoryResponseDto = categoryMapper.convertToDto(category);
        logger.info("Category with id {} was found succsessfully",id);
        return categoryResponseDto ;
    }

    @Override
    public CategoryResponseDto save(CategoryRequestDto categoryRequestDto) {

        logger.info("Attempting to create a category");

        Category category = categoryRepository
                .save(categoryMapper.convertToEntity(categoryRequestDto));

        CategoryResponseDto categoryResponseDto = categoryMapper.convertToDto(category);
        logger.info("Category created successfully");

        return categoryResponseDto;
    }

    @Override
    public CategoryResponseDto update(CategoryRequestDto categoryRequestDto, Long id) {

        logger.info("Attempting to update category with id:{}",id);

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(()-> {
                    logger.warn("Failed to find a category with id:{}",id);
                    return new ResourceNotFoundException("Cannot found category with id:" + id);
                });
        category.setName(categoryRequestDto.name());
        category.setDescription(categoryRequestDto.description());

        CategoryResponseDto categoryResponseDto = categoryMapper.convertToDto(categoryRepository.save(category));
        logger.info("Category with id {} was updated succsessfully",id);

        return categoryResponseDto;
    }

    @Override
    public void delete(Long id) {

        logger.info("Attempting to delete category with id:{}",id);

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(()-> {
                    logger.warn("Failed to find a category with id:{}",id);
                    return new ResourceNotFoundException("Cannot found category with id:" + id);
                });
        categoryRepository.delete(category);
        logger.info("Category with id {} was deleted succsessfully",id);
    }
}
