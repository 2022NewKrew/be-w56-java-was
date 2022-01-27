package application.out.user;

import domain.user.User;

import java.util.Optional;

public interface FindUserPort {

    Optional<User> findByUserId(String userId);
}
