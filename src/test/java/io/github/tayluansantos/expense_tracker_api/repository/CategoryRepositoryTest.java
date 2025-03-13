package io.github.tayluansantos.expense_tracker_api.repository;

import io.github.tayluansantos.expense_tracker_api.model.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setup(){
        category = new Category(
            "Category Test",
                "Category description test"
        );
    }

    @DisplayName("Return category when found by ID")
    @Test
    void givenCategoryId_WhenFindById_ThenReturnACategory() {

        //Given
        Category categorySaved = categoryRepository.save(category);
        Long categoryId = categorySaved.getId();

        //When
        Optional<Category> result = categoryRepository.findById(categoryId);

        //Then
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(categorySaved.getId(), result.get().getId());
    }

    @DisplayName("Return empty optional when category not found")
    @Test
    void givenInvalidId_WhenFindById_ThenReturnEmpty() {
        //Given
        Long invalidId = 999L;

        //When
        Optional<Category> result = categoryRepository.findById(invalidId);

        //Then
        Assertions.assertTrue(result.isEmpty());
    }

    @DisplayName("Return list of all categories")
    @Test
    void whenFindAll_ThenReturnCategoryList() {

        //Given
        Category savedUser = categoryRepository.save(category);

        //When
        List<Category> result = categoryRepository.findAll();

        //Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
    }

    @DisplayName("Save and return category")
    @Test
    void givenCategoryObject_WhenSave_ThenReturnSavedCategory() {

        //When
        Category response = categoryRepository.save(category);

        //Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Category Test", category.getName());
        Assertions.assertEquals("Category description test", category.getDescription());
    }

    @DisplayName("Update and return category")
    @Test
    void givenCategoryObject_WhenUpdate_ThenReturnUpdatedCategory() {

        //Given
        Category savedCategory = categoryRepository.save(category);
        savedCategory.setName("Category Test - Updated");

        //When
        Category result = categoryRepository.save(savedCategory);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(savedCategory.getId(),result.getId());
        Assertions.assertEquals("Category Test - Updated", category.getName());
        Assertions.assertEquals("Category description test", category.getDescription());

    }

    @DisplayName("Delete category by ID")
    @Test
    void givenCategoryId_WhenDelete_ThenReturnRemoveCategory() {

        //Given
        Category categorySaved = categoryRepository.save(category);
        Long userId = categorySaved.getId();

        //When
        categoryRepository.deleteById(userId);
        Optional<Category> optionalCategory = categoryRepository.findById(userId);

        //Then
        Assertions.assertTrue(optionalCategory.isEmpty());
    }
}
