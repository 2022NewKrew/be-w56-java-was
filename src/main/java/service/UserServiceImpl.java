package service;

import java.util.Optional;
import repository.UserRepository;
import java.util.List;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceImpl implements UserService{

    String LOGIN_SUCCESS = "{} login success";
    String LOGIN_FAIL = "{} login fail";
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> login(User user) {
        Optional<User> expectUser = userRepository.findUserById(user.getUserId());

        if (expectUser.isPresent() && expectUser.get().checkPassword(user.getPassword())) {
            logger.info(LOGIN_SUCCESS, user.getUserId());
            return Optional.ofNullable(user);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
