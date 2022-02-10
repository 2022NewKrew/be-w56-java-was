package app.user.application.port.out;

import app.user.domain.User;
import java.util.Collection;

public interface LoadUserPort {

    User findUserById(String userId);

    Collection<User> findAll();

}
