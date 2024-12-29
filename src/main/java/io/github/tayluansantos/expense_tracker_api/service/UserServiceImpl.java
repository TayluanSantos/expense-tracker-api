package io.github.tayluansantos.expense_tracker_api.service;

import io.github.tayluansantos.expense_tracker_api.dto.user.UserRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.user.UserResponseDto;
import io.github.tayluansantos.expense_tracker_api.mapper.IUserMapper;
import io.github.tayluansantos.expense_tracker_api.model.User;
import io.github.tayluansantos.expense_tracker_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IUserMapper userMapper;

    @Override
    public UserResponseDto save(UserRequestDto userRequest) {

        if(userRepository.findByEmail(userRequest.email()) != null) {
            throw new RuntimeException("This email already exist.");
        }

        User user = userMapper.userDtoToUser(userRequest);
        userRepository.save(user);

        return userMapper.userToUserDto(user);

    }

    @Override
    public User update(User userRequest, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());

        return userRepository.save(user);
    }

    @Override
    public UserResponseDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));
        return userMapper.userToUserDto(user);
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userMapper.convertListEntityToListDto(userRepository.findAll());
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));
        userRepository.delete(user);
    }
}
