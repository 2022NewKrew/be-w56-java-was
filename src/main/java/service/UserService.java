package service;

import domain.UserId;
import dto.UserDto;
import entity.UserEntity;
import exception.CreateUserException;
import mapper.UserMapper;
import repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserService {

    private static final UserService userService = new UserService();
    private final UserRepository userRepository = UserRepository.getInstance();

    private UserService() {
    }

    public static UserService getInstance() {
        return userService;
    }

    public void save(UserDto userDto) throws CreateUserException {
        UserId userId = userDto.getUserId();
        if (userRepository.findById(userId.getUserId()) != null) {
            throw new CreateUserException("이미 존재하는 아이디입니다.");
        }
        userRepository.save(UserMapper.toUserEntity(userDto));
    }

    public UserDto findById(UserId userId) {
        UserEntity find = userRepository.findById(userId.getUserId());
        return UserMapper.toUserDto(find);
    }

    public Collection<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public boolean validateUser(UserDto user) {
        UserDto find = findById(user.getUserId());
        return find != null && find.getPassword().equals(user.getPassword());
    }


}
