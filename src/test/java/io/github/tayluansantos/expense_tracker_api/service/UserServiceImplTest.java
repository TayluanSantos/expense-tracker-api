package io.github.tayluansantos.expense_tracker_api.service;

import io.github.tayluansantos.expense_tracker_api.dto.user.UserRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.user.UserResponseDto;
import io.github.tayluansantos.expense_tracker_api.exception.EmailAlreadyExistException;
import io.github.tayluansantos.expense_tracker_api.exception.ResourceNotFoundException;
import io.github.tayluansantos.expense_tracker_api.mapper.IUserMapper;
import io.github.tayluansantos.expense_tracker_api.model.Role;
import io.github.tayluansantos.expense_tracker_api.model.UserModel;
import io.github.tayluansantos.expense_tracker_api.repository.UserRepository;
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
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository  userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private IUserMapper userMapper;

    private UserModel user;
    private UserRequestDto userRequestDto;
    private UserResponseDto userResponseDto;


    @BeforeEach
    void setUp(){
        user = new UserModel();
        user.setId(1L);
        user.setName("User Test");
        user.setEmail("usertest@gmail.com");
        user.setPassword("encoded_password");
        user.setRole(Role.ROLE_USER);
        user.setExpenses(null);

        userRequestDto = new UserRequestDto("User Test",
                "usertest@gmail.com",
                "user1234",
                Role.ROLE_USER);

        userResponseDto = new UserResponseDto(
                1L,
                "User Test",
                null
        );
    }

    @Test
    @DisplayName("save - Should create and return new user")
    void givenUserRequestDtoObject_WhenSave_ThenReturnTheSavedUser(){

        //given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("password_encoded");
        when(userMapper.userDtoToUser(any(UserRequestDto.class))).thenReturn(user);
        when(userRepository.save(any(UserModel.class))).thenReturn(user);
        when(userMapper.userToUserDto(any(UserModel.class))).thenReturn(userResponseDto);

        //when
        UserResponseDto result = userService.save(userRequestDto);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(userResponseDto.id(), result.id());
        Assertions.assertEquals(userResponseDto.name(), result.name());
    }

    @Test
    @DisplayName("save - Should throw EmailAlreadyExistException when email already exist")
    void givenExistedEmail_WhenSave_ThenThrowEmailAlreadyExistException() {

        //given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        //when
        Assertions.assertThrows(EmailAlreadyExistException.class,() -> {
            userService.save(userRequestDto);
        });

        //then
        verify(userRepository,times(1)).findByEmail(userRequestDto.email());
        verifyNoInteractions(userMapper,passwordEncoder);
    }

    @Test
    @DisplayName("update - Should update existing user")
    void givenUserRequestDtoObject_WhenUpdate_ThenReturnUpdatedUser() {

        //given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserModel.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("new_encoded_password");
        when(userMapper.userToUserDto(any(UserModel.class))).thenReturn(userResponseDto);

        //when
        UserResponseDto result = userService.update(userRequestDto, 1L);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(userResponseDto.id(), result.id());

        verify(userRepository,times(1)).findById(1L);
        verify(passwordEncoder,times(1)).encode(userRequestDto.password());
        verify(userRepository,times(1)).save(user);
        verify(userMapper,times(1)).userToUserDto(user);
    }

    @Test
    @DisplayName("findById - Should return user when found")
    void givenUserId_WhenFindById_ThenReturnTheUser() {

        //given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userMapper.userToUserDto(any(UserModel.class))).thenReturn(userResponseDto);

        //when
        UserResponseDto result = userService.findById(1L);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(userResponseDto.id(), result.id());
        Assertions.assertEquals(userResponseDto.name(), result.name());

        verify(userRepository,times(1)).findById(1L);
        verify(userMapper,times(1)).userToUserDto(user);
    }

    @Test
    @DisplayName("findById - Should throw ResourceNotFoundException when user not found")
    void givenUnexistedUserId_WhenFindById_ThenThrowResourceNotFoundException() {

        //given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            userService.update(userRequestDto, 1L);
        });

        //then
        verify(userRepository,times(1)).findById(1L);
        verifyNoMoreInteractions(userMapper, passwordEncoder);
    }

    @Test
    @DisplayName("findAll - Should return list of users")
    void whenFindAll_ThenReturnAllUsers() {

        //given
        List<UserResponseDto> userListResponse = List.of(userResponseDto);
        List<UserModel> userList = List.of(user);

        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.convertListEntityToListDto(ArgumentMatchers.<UserModel>anyList())).thenReturn(userListResponse);

        //when
        List<UserResponseDto> result = userService.findAll();

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1,result.size());
        Assertions.assertEquals("User Test",result.get(0).name());
    }

    @Test
    @DisplayName("delete - Should delete existing user")
    void whenDelete_ThenRemoveAUser() {

        //given
        doNothing().when(userRepository).delete(any(UserModel.class));

        //when
        userRepository.delete(user);

        //then
        verify(userRepository).delete(user);
    }
}
