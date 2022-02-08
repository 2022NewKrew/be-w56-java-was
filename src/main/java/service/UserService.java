package service;

import dto.UserCreateDto;
import dto.UserResponseDto;
import dto.mapper.UserMapper;
import model.User;
import model.repository.UserRepository;
import model.repository.UserRepositoryMap;

import java.util.List;

public class UserService {
    private static final UserService instance = new UserService();

    private UserService() {}

    public static UserService getInstance() {
        return instance;
    }

    private static final UserRepository userRepository = new UserRepositoryMap();

    public void create(UserCreateDto userCreateDto){
        userRepository.save(UserMapper.INSTANCE.toEntityFromSaveDto(userCreateDto));
    }

    public List<UserResponseDto> findAll(){
        return UserMapper.INSTANCE.toDtoList(userRepository.findAll());
    }

    public Boolean login(String stringId, String password){
        User user = userRepository.findByStringId(stringId);
        return user != null && user.getPassword().equals(password);
    }
}
