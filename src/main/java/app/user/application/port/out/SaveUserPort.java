package app.user.application.port.out;

import app.user.domain.User;

public interface SaveUserPort {

    void save(User user);
    
}
