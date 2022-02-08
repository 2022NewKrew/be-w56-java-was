package service;

import dto.UserLoginRequest;
import dto.UserSignupRequest;
import exception.AlreadyExistsException;
import exception.UserNotFoundException;
import model.User;
import repository.UserRepository;

public class UserService {

    private final UserRepository userRepository = new UserRepository();

    public void signup(UserSignupRequest request) {
        User user = request.toEntity();
        userRepository.findById(user.getUserId()).ifPresent(u -> {
            throw new AlreadyExistsException(u.getUserId() + "는 이미 존재하는 아이디입니다.");
        });
        userRepository.save(user);
    }

    public void login(UserLoginRequest request) throws UserNotFoundException {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(UserNotFoundException::new);
        if (!user.matches(request.getPassword())) {
            throw new UserNotFoundException();
        }
    }
}
