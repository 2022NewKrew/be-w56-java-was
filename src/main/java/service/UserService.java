package service;

import dto.UserCreateDto;
import dto.mapper.UserMapper;
import model.repository.UserRepository;
import model.repository.UserRepositoryList;
import webserver.controller.UserController;

public class UserService {
    private static final UserService instance = new UserService();

    private UserService() {}

    public static UserService getInstance() {
        return instance;
    }

    private static final UserRepository userRepository = new UserRepositoryList();

    public void create(UserCreateDto userCreateDto){
        userRepository.save(UserMapper.INSTANCE.toEntityFromSaveDto(userCreateDto));
        userRepository.findAll().forEach(System.out::println);
    }
}
