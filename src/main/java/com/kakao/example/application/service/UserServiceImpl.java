package com.kakao.example.application.service;

import com.kakao.example.application.dto.UserDto;
import com.kakao.example.model.domain.User;
import com.kakao.example.model.repository.UserRepository;
import com.kakao.example.model.repository.UserRepositoryMemoryImpl;
import com.kakao.example.util.exception.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {
    private static UserService instance;

    private final UserRepository userRepository = UserRepositoryMemoryImpl.getInstance();
    private final ModelMapper modelMapper = new ModelMapper();

    public UserServiceImpl() {
        modelMapper.getConfiguration()
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public static UserService getInstance() {
        instance = new UserServiceImpl();
        return instance;
    }

    @Override
    public void addUser(User user) {
        userRepository.addUser(user);
    }

    @Override
    public UserDto findUserById(String userId) {
        return userRepository.findUserById(userId).stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .findFirst()
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserDto findUserByLoginInfo(String userId, String password) {
        return userRepository.findUserByLoginInfo(userId, password).stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .findFirst()
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Collection<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }
}
