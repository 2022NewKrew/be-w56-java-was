package service;

import db.H2EntityManagerFactory;
import domain.Email;
import domain.Name;
import domain.Password;
import domain.UserId;
import dto.MemoDto;
import dto.UserDto;
import exception.CreateUserException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.Persistence;
import java.time.LocalDateTime;

class MemoServiceTest {

    private final MemoService memoService = MemoService.getInstance();
    private final UserService userService = UserService.getInstance();

    @BeforeAll
    static void init() {
        H2EntityManagerFactory.emf = Persistence.createEntityManagerFactory("java-was");
    }

    @Test
    void crud() throws CreateUserException {
        UserId userId = new UserId("userId");
        Password password = new Password("password");
        Name name = new Name("name");
        Email email = new Email("email");
        UserDto userDto = new UserDto(userId, password, name, email, LocalDateTime.now());
        userService.save(userDto);
        MemoDto memoDto = new MemoDto("name", "contents", userDto);
        memoService.save(memoDto);
        memoDto = new MemoDto("name", "contents2", userDto);
        memoService.save(memoDto);
        memoDto = new MemoDto("name", "contents3", userDto);
        memoService.save(memoDto);
        memoService.findAll().forEach(System.out::println);
    }
}
