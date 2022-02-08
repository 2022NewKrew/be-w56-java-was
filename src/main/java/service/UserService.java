package service;

import dto.AuthDto;
import dto.UserDto;
import exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.User;
import repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class UserService {
    private static final UserService INSTANCE = new UserService();

    public static UserService getInstance() {
        return INSTANCE;
    }

    private final UserRepository userRepository;

    private UserService() {
        userRepository = UserRepository.getInstance();
    }

    public void register(UserDto dto) {
        if (dto.getUserId() == null || dto.getPassword() == null || dto.getName() == null || dto.getEmail() == null) {
            throw new UserException();
        }
        userRepository.save(dtoToEntity(dto));
    }

    public AuthDto login(UserDto dto) {
        User user = userRepository.findById(dtoToEntity(dto).getUserId())
                .filter(entity -> entity.getPassword().equals(dto.getPassword()))
                .orElseThrow(UserException::new);
        return entityToAuthDto(user);
    }

    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserException::new);
        return entityToDto(user);
    }

    public List<UserDto> getAllUser() {
        return userRepository.findAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public void update(UserDto dto) {
        User user = userRepository.findById(dtoToEntity(dto).getUserId())
                .filter(entity -> entity.getPassword().equals(dto.getPassword()))
                .orElseThrow(UserException::new);
        user.changePassword(dto.getPassword());
        user.changeName(dto.getName());
        user.changeEmail(dto.getEmail());
        userRepository.update(user);
    }

    private User dtoToEntity(UserDto dto) {
        return User.builder()
                .userId(dto.getUserId())
                .password(dto.getPassword())
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    private UserDto entityToDto(User entity) {
        return UserDto.builder()
                .userId(entity.getUserId())
                .password(entity.getPassword())
                .name(entity.getName())
                .email(entity.getEmail())
                .build();
    }

    private AuthDto entityToAuthDto(User entity) {
        return AuthDto.builder()
                .userId(entity.getUserId())
                .name(entity.getName())
                .email(entity.getEmail())
                .build();
    }
}
