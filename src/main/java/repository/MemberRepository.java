package repository;

import model.User;

import java.util.Optional;

public interface MemberRepository {
    void insert(User user);

    Optional<User> findByUsername(String username);
}
