package application.out.user;

import domain.user.User;

import java.util.List;
import java.util.Optional;

public interface FindUserPort {
    Optional<User> findByUserId(String userId);
    List<User> findAll();
}
