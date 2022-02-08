package repository;

import db.H2EntityManagerFactory;
import entity.UserEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

class UserRepositoryTest {

    private final UserRepository userRepository = UserRepository.getInstance();

    @BeforeAll
    static void init() {
        H2EntityManagerFactory.emf = Persistence.createEntityManagerFactory("java-was");
    }

    @Test
    void crud() {
        UserEntity userEntity = new UserEntity("userId", "password", "name", "email", LocalDateTime.now());
        userRepository.save(userEntity);
        UserEntity userEntity2 = new UserEntity("userId2", "password2", "name2", "email2", LocalDateTime.now());
        userRepository.save(userEntity2);
        UserEntity findEntity = userRepository.findById("userId");
        System.out.println(findEntity);
        List<UserEntity> userEntities = userRepository.findAll();
        userEntities.forEach(System.out::println);
    }

}
