package io.github.tayluansantos.expense_tracker_api.service;

import io.github.tayluansantos.expense_tracker_api.dto.category.CategoryRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.category.CategoryResponseDto;
import io.github.tayluansantos.expense_tracker_api.exception.ResourceNotFoundException;
import io.github.tayluansantos.expense_tracker_api.mapper.ICategoryMapper;
import io.github.tayluansantos.expense_tracker_api.model.Category;
import io.github.tayluansantos.expense_tracker_api.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ICategoryMapper categoryMapper;

    private Category category;
    private CategoryRequestDto categoryRequestDto;
    private CategoryResponseDto categoryResponseDto;


    @BeforeEach
    void setUp(){
        category = new Category();
        category.setId(1L);
        category.setName("Category Test");
        category.setDescription("Category description");

        categoryRequestDto = new CategoryRequestDto(
                "Category Test",
                "Category description"
        );

        categoryResponseDto = new CategoryResponseDto(
                1L,
                "Category Test",
                "Category description"
        );
    }

    @Test
    @DisplayName("save - Should create and return new category")
    void givenCategoryRequestDtoObject_WhenSave_ThenReturnTheSavedCategory(){

        //given
        when(categoryMapper.convertToEntity(any(CategoryRequestDto.class))).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.convertToDto(any(Category.class))).thenReturn(categoryResponseDto);

        //when
        CategoryResponseDto result = categoryService.save(categoryRequestDto);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(category.getName(), result.name());
        Assertions.assertEquals(category.getDescription(), result.description());
    }


    @Test
    @DisplayName("update - Should update existing category")
    void givenCategoryRequestDtoObject_WhenUpdate_ThenReturnUpdatedCategory() {

        //given
        CategoryRequestDto categoryRequestUpdate = new CategoryRequestDto(
                "Category Test - Update",
                "Category description - Update");

        Category categoryUpdate = new Category();
        categoryUpdate.setId(1L);
        categoryUpdate.setName("Category Test - Update");
        categoryUpdate.setDescription("Category description - Update");

        CategoryResponseDto categoryUpdateResponseDto = new CategoryResponseDto(
                1L,
                "Category Test - Update",
                "Category description - Update");

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(categoryUpdate);
        when(categoryMapper.convertToDto(categoryUpdate)).thenReturn(categoryUpdateResponseDto);

        //when
        CategoryResponseDto result = categoryService.update(categoryRequestUpdate, 1L);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(categoryUpdateResponseDto.id(), result.id());
        Assertions.assertEquals(categoryUpdateResponseDto.name(), result.name());
        Assertions.assertEquals(categoryUpdateResponseDto.description(), result.description());

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(categoryMapper, times(1)).convertToDto(categoryUpdate);
    }

    @Test
    @DisplayName("findById - Should return category when found")
    void givenCategoryId_WhenFindById_ThenReturnTheCategory() {

        //given
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryMapper.convertToDto(any(Category.class))).thenReturn(categoryResponseDto);

        //when
        CategoryResponseDto result = categoryService.findById(1L);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(categoryResponseDto.id(), result.id());
        Assertions.assertEquals(categoryResponseDto.name(), result.name());
        Assertions.assertEquals(categoryResponseDto.description(), result.description());

        verify(categoryRepository,times(1)).findById(1L);
        verify(categoryMapper,times(1)).convertToDto(category);
    }

    @Test
    @DisplayName("findById - Should throw ResourceNotFoundException when category not found")
    void givenUnexistedCategoryId_WhenFindById_ThenThrowResourceNotFoundException() {

        //given
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        //then
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.update(categoryRequestDto, 1L);
        });

        verify(categoryRepository,times(1)).findById(1L);
        verifyNoMoreInteractions(categoryMapper, passwordEncoder);
    }

    @Test
    @DisplayName("findAll - Should return list of categories")
    void whenFindAll_ThenReturnAllCategories() {

        //given
        List<CategoryResponseDto> categoryResponseDtoList = List.of(categoryResponseDto);
        List<Category> categoryList = List.of(category);

        when(categoryRepository.findAll()).thenReturn(categoryList);
        when(categoryMapper.convertListEntityToListDto(ArgumentMatchers.<Category>anyList())).thenReturn(categoryResponseDtoList);

        //when
        List<CategoryResponseDto> result = categoryService.findAll();

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1,result.size());
        Assertions.assertEquals(categoryResponseDto.name(),result.get(0).name());
        Assertions.assertEquals(categoryResponseDto.description(),result.get(0).description());
    }

    @Test
    @DisplayName("delete - Should delete existing category")
    void whenDelete_ThenRemoveACategory() {

        //given
        doNothing().when(categoryRepository).delete(any(Category.class));

        //when
        categoryRepository.delete(category);

        //then
        verify(categoryRepository).delete(category);
    }
}
