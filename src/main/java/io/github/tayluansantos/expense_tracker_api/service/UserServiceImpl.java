package io.github.tayluansantos.expense_tracker_api.service;

import io.github.tayluansantos.expense_tracker_api.dto.user.UserRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.user.UserResponseDto;
import io.github.tayluansantos.expense_tracker_api.exception.EmailAlreadyExistException;
import io.github.tayluansantos.expense_tracker_api.exception.ResourceNotFoundException;
import io.github.tayluansantos.expense_tracker_api.mapper.IUserMapper;
import io.github.tayluansantos.expense_tracker_api.model.UserModel;
import io.github.tayluansantos.expense_tracker_api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final IUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, IUserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDto save(UserRequestDto userRequest) {
        logger.info("Attempting to create user with email: {}", userRequest.email());

        Optional<UserModel> user = userRepository.findByEmail(userRequest.email());

        if(user.isPresent()){
            logger.error("Failed to create user.This email already exists");
            throw new EmailAlreadyExistException("This email already exist.");
        }

        UserModel userModel = userMapper.userDtoToUser(userRequest);
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userModel.setRole(userRequest.role());
        userRepository.save(userModel);

        logger.info("User created successfully with email: {}", userRequest.email());
        return userMapper.userToUserDto(userModel);
    }

    @Override
    public UserResponseDto update(UserRequestDto userRequest, Long id) {
        logger.info("Attempting to update user with id: {}", id);

        UserModel userModel = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Error: User with id {} not found", id);
                    return new ResourceNotFoundException("Cannot find user with id: " + id);
                });

        userModel.setName(userRequest.name());
        userModel.setEmail(userRequest.email());

        if (userRequest.password() != null && !userRequest.password().isEmpty()) {
            userModel.setPassword(passwordEncoder.encode(userRequest.password()));
        }

        userRepository.save(userModel);
        logger.info("User with id {} was updated successfully", id);
        return userMapper.userToUserDto(userModel);
    }

    @Override
    public UserResponseDto findById(Long id) {
        logger.info("Attempting to find user with id: {}", id);

        UserModel userModel = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Error: User with id {} not found", id);
                    return new ResourceNotFoundException("Cannot find user with id: " + id);
                });

        logger.info("User with id: {} was found successfully", id);
        return userMapper.userToUserDto(userModel);
    }

    @Override
    public List<UserResponseDto> findAll() {
        logger.info("Retrieving all users");
        return userMapper.convertListEntityToListDto(userRepository.findAll());
    }

    @Override
    public void delete(Long id) {
        logger.info("Attempting to delete user with id: {}", id);

        UserModel userModel = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Error: User with id {} not found", id);
                    return new ResourceNotFoundException("Cannot find user with id: " + id);
                });

        userRepository.delete(userModel);
        logger.info("User with id: {} was deleted successfully", id);
    }
}
