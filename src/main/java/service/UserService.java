package service;

import dto.UserCreateDto;
import dto.mapper.UserMapper;
import model.repository.UserRepository;
import model.repository.UserRepositoryList;

public class UserService {
    private static final UserRepository userRepository = new UserRepositoryList();

    public void create(UserCreateDto userCreateDto){
        userRepository.save(UserMapper.INSTANCE.toEntityFromSaveDto(userCreateDto));
        userRepository.findAll().stream().map(s->s.toString()).forEach(System.out::println);
    }
}
