package service;

import db.UserRepository;
import java.util.List;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(User user) {
        User expectedUser = userRepository.findById(user.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 유저입니다."));

        if (expectedUser.checkPassword(user.getPassword())) {
            LOG.info("{} login success", user.getUserId());
            return expectedUser;
        }
        throw new IllegalArgumentException("[ERROR] 로그인 실패");
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void save(User user) {
        userRepository.addUser(user);
        LOG.info("{} user create success", user.getUserId());
    }
}
