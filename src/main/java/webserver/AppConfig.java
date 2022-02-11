package webserver;

import repository.UserRepository;
import repository.MemoryUserRepository;
import service.UserService;
import service.UserServiceImpl;

public class AppConfig {

    public final String HOME = "/index.html";
    public static final AppConfig appConfig = new AppConfig();

    public UserRepository userRepository() {
        return new MemoryUserRepository();
    }

    public UserService userService() {
        return new UserServiceImpl(userRepository());
    }

    public RequestMapper requestMapping() {
        return new RequestMapper();
    }
}
