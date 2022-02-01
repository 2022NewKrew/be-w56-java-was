package com.kakao.example.application.service;

import com.kakao.example.application.dto.UserDto;
import com.kakao.example.model.domain.User;
import com.kakao.example.model.repository.UserRepository;
import com.kakao.example.util.exception.UserNotFoundException;
import framework.util.annotation.Autowired;
import framework.util.annotation.Component;
import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.stream.Collectors;

import static framework.util.annotation.Component.ComponentType.SERVICE;

@Component(type = SERVICE)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
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
