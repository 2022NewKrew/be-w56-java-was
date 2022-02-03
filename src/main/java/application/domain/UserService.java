package application.domain;

import di.annotation.Bean;

import java.util.List;

@Bean
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void create(User user){
        userRepository.save(user);
    }

    public boolean login(String name, String pwd){
        final User user = userRepository.findById(name)
                .orElseThrow();

        return user.validate(pwd);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }
}
