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
        Long userId = savedUser.getId(); // Pegando o ID real gerado

        //When
        UserModel response = userRepository.findById(userId).get();

        //Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(savedUser.getId(), response.getId());
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
        UserModel response = userRepository.save(user);

        //Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals("User Test", user.getName());
        Assertions.assertEquals("usertest@gmail.com", user.getEmail());
        Assertions.assertEquals("user1234", user.getPassword());
        Assertions.assertEquals(Role.ROLE_USER, user.getRole());

    }

    @DisplayName("Update and return user")
    @Test
    void givenUserObject_WhenUpdate_ThenReturnUpdatedUser() {

        //Given
        user.setName("User Test - Updated");

        //When
        UserModel response = userRepository.save(user);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("User Test - Updated", user.getName());
        Assertions.assertEquals("usertest@gmail.com", user.getEmail());
        Assertions.assertEquals("user1234", user.getPassword());
        Assertions.assertEquals(Role.ROLE_USER, user.getRole());

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
