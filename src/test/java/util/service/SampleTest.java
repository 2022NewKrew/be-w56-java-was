package util.service;

import dto.UserDto;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class SampleTest {
    @Test
    void test() throws NoSuchFieldException, IllegalAccessException {
        UserDto dto = UserDto.builder()
                .userId("kusakina")
                .password("1234")
                .email("kusakina0608@hanmail.net")
                .name("kina")
                .build();
        Class cls = dto.getClass();
        Field field = cls.getDeclaredField("userId");
        field.setAccessible(true);
        System.out.println(field.get(dto).toString());
    }
}
