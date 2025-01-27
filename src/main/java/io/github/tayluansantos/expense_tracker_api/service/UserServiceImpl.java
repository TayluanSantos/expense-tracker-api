package io.github.tayluansantos.expense_tracker_api.service;

import io.github.tayluansantos.expense_tracker_api.dto.user.UserRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.user.UserResponseDto;
import io.github.tayluansantos.expense_tracker_api.exception.EmailAlreadyExistException;
import io.github.tayluansantos.expense_tracker_api.exception.ResourceNotFoundException;
import io.github.tayluansantos.expense_tracker_api.mapper.IUserMapper;
import io.github.tayluansantos.expense_tracker_api.model.UserModel;
import io.github.tayluansantos.expense_tracker_api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService{

    private final UserRepository userRepository;
    private final IUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           IUserMapper userMapper,PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDto save(UserRequestDto userRequest) {

        if(userRepository.findByEmail(userRequest.email()) != null) {
            throw new EmailAlreadyExistException("This email already exist.");
        }

        UserModel userModel = userMapper.userDtoToUser(userRequest);
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userRepository.save(userModel);

        return userMapper.userToUserDto(userModel);

    }

    @Override
    public UserResponseDto update(UserRequestDto userRequest, Long id) {
        UserModel userModel = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found user with id: " + id));
        userModel.setName(userRequest.name());
        userModel.setEmail(userRequest.email());
        userModel.setPassword(userRequest.password());

        userRepository.save(userModel);

        return userMapper.userToUserDto(userModel);
    }

    @Override
    public UserResponseDto findById(Long id) {
        UserModel userModel = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found user with id: " + id));
        return userMapper.userToUserDto(userModel);
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userMapper.convertListEntityToListDto(userRepository.findAll());
    }

    @Override
    public void delete(Long id) {
        UserModel userModel = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found user with id: " + id));
        userRepository.delete(userModel);
    }
}
