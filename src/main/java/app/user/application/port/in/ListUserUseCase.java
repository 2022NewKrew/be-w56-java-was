package app.user.application.port.in;

import app.user.domain.User;
import java.util.Collection;

public interface ListUserUseCase {

    Collection<User> listAll();

}
