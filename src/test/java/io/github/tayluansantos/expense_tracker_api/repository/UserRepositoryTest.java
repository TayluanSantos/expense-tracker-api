package io.github.tayluansantos.expense_tracker_api.repository;

import io.github.tayluansantos.expense_tracker_api.model.Role;
import io.github.tayluansantos.expense_tracker_api.model.UserModel;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private UserModel user;

    @BeforeEach
    void setUp() {
        user = new UserModel(
                "User Test",
                "usertest@gmail.com",
                "user1234",
                Role.ROLE_USER
        );
    }

    @DisplayName("Return user when found by ID")
    @Test
    void givenUserId_WhenFindById_ThenReturnUser() {

        //Given
        UserModel savedUser = userRepository.save(user);
        Long userId = savedUser.getId(); 

        //When
        Optional<UserModel> result = userRepository.findById(userId);

        //Then
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(savedUser.getId(), result.get().getId());
    }

    @DisplayName("Return empty optional when user not found")
    @Test
    void givenInvalidId_WhenFindById_ThenReturnEmpty() {
        //Given
        Long invalidId = 999L;

        //When
        Optional<UserModel> result = userRepository.findById(invalidId);

        //Then
        Assertions.assertTrue(result.isEmpty());
    }

    @DisplayName("Return list of all users")
    @Test
    void whenFindAll_ThenReturnUserList() {

        //Given
        UserModel savedUser = userRepository.save(user);

        //When
        List<UserModel> response = userRepository.findAll();

        //Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.size());
    }

    @DisplayName("Save and return user")
    @Test
    void givenUserObject_WhenSave_ThenReturnSavedUser() {

        //When
        UserModel result = userRepository.save(user);

        //Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals("User Test", result.getName());
        Assertions.assertEquals("usertest@gmail.com", result.getEmail());
        Assertions.assertEquals("user1234", result.getPassword());
        Assertions.assertEquals(Role.ROLE_USER, result.getRole());

    }

    @DisplayName("Update and return user")
    @Test
    void givenUserObject_WhenUpdate_ThenReturnUpdatedUser() {

        //Given
        UserModel savedUser = userRepository.save(user);
        user.setName("User Test - Updated");

        //When
        UserModel result = userRepository.save(user);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(savedUser.getId(),result.getId());
        Assertions.assertEquals("User Test - Updated", result.getName());
        Assertions.assertEquals("usertest@gmail.com", result.getEmail());
        Assertions.assertEquals("user1234", result.getPassword());
        Assertions.assertEquals(Role.ROLE_USER, result.getRole());

    }

    @DisplayName("Delete user by ID")
    @Test
    void givenUserId_WhenDelete_ThenReturnRemoveUser() {

        //Given
        UserModel savedUser = userRepository.save(user);
        Long userId = savedUser.getId(); // Pegando o ID real gerado

        //When
        userRepository.deleteById(userId);
        Optional<UserModel> optionalUser = userRepository.findById(userId);

        //Then
        Assertions.assertTrue(optionalUser.isEmpty());
    }
}
