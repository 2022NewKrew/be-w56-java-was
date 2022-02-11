package application.domain;

import application.db.repository.UserRepository;
import application.domain.dto.LoginDto;

import java.util.List;

public class UserService {

    public final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(User user) {
        userRepository.addUser(user);
    }

    public boolean login(LoginDto loginUser) {
        User findUser;
        findUser = userRepository.findUserById(loginUser.getUserId());
        if ( findUser == null || !findUser.getPassword().equals(loginUser.getPassword()) )
            return false;
        return true;
    }

    public List<User> getUserList() {
        return userRepository.findAll();
    }
}
