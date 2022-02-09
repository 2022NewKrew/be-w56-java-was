package data;

import data.converter.ConverterServiceForTest;
import org.junit.jupiter.params.provider.Arguments;
import webserver.domain.entity.User;

import java.util.List;
import java.util.stream.Stream;

public class MockUserData {
    public static Stream<Arguments> getFieldMapStream(){
        List<User> users = MockUserData.getAll();
        return ConverterServiceForTest.convertToFieldMapListArgumentsStream(users);
    }

    public static List<User> getAll() {
        return List.of(
                new User("javajigi", "password", "javajigi", "java@daum.net"),
                new User("hello", "password2", "hello", "hello@daum.net"),
                new User("hihi", "hihi1234", "hihi", "hihi@daum.net"),
                new User("goodman", "good1234", "good", "good@daum.net")
        );
    }
}
