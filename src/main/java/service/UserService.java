package service;

import dto.UserCreateDto;
import dto.UserResponseDto;
import dto.mapper.UserCookieDto;
import dto.mapper.UserMapper;
import model.User;
import model.repository.user.UserRepository;
import model.repository.user.UserRepositoryJdbc;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class UserService {
    private static final UserService instance = new UserService();

    private UserService() {}

    public static UserService getInstance() {
        return instance;
    }

    private static final UserRepository userRepository = new UserRepositoryJdbc();

    public void create(UserCreateDto userCreateDto){
        userRepository.save(UserMapper.INSTANCE.toEntityFromSaveDto(userCreateDto));
    }

    public List<UserResponseDto> findAll(){
        return UserMapper.INSTANCE.toDtoList(userRepository.findAll());
    }

    public UserCookieDto login(String stringId, String password){
        User user = userRepository.findByStringId(stringId);
        if(user!=null && StringUtils.equals(user.getPassword(), password)){
            return UserMapper.INSTANCE.toCookieDto(user);
        }
        throw new IllegalArgumentException();
    }
}
