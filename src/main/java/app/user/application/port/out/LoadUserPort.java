package app.user.application.port.out;

import app.user.domain.User;
import app.user.domain.UserId;
import java.util.Collection;

public interface LoadUserPort {

    User findUserById(UserId userId);

    Collection<User> findAll();

}
