package springmvc.repository;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final List<User> memoryDatabase = new ArrayList<>();
    private final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    public void save(User user) {
        memoryDatabase.add(user);
        logger.debug("success save!!");
    }
}
