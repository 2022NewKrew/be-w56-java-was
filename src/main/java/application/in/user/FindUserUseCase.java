package application.in.user;

import domain.user.User;

import java.util.List;

public interface FindUserUseCase {
    List<User> findAll();
}
