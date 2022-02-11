package cafe.service;

import cafe.controller.exception.IncorrectLoginUserException;
import cafe.dto.UserDto;
import cafe.model.User;
import cafe.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public List<UserDto> getUserList() throws IOException {
        return userRepository.findAll().stream().map(UserDto::of).collect(Collectors.toList());
    }

    public void createUser(String userId, String password, String name, String email) {
        User user = new User(userId, password, name, email);
        userRepository.addUser(user);
    }

    public void authenticateUser(String userId, String password) throws IncorrectLoginUserException {
        User user = userRepository.findUserById(userId);

        if (user == null || !user.isValidPassword(password)) {
            throw new IncorrectLoginUserException();
        }
    }
}
