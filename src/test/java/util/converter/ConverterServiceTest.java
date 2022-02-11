package util.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.domain.entity.Post;
import webserver.domain.entity.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class ConverterServiceTest {
    @ParameterizedTest
    @DisplayName("BodyParams에서 User로 Convert 성공")
    @MethodSource("data.MockUserData#getFieldMapStream")
    void successConvertBodyParamsToUser(Map<String, String> bodyParams) {
        //given

        //when
        User user = ConverterService.convert(bodyParams, User.class);

        //then
        assertThat(user.getUserId()).isEqualTo(bodyParams.get("userId"));
        assertThat(user.getPassword()).isEqualTo(bodyParams.get("password"));
        assertThat(user.getName()).isEqualTo(bodyParams.get("name"));
        assertThat(user.getEmail()).isEqualTo(bodyParams.get("email"));
    }

    @ParameterizedTest
    @DisplayName("BodyParams에서 Post로 Convert 성공")
    @MethodSource("data.MockPostData#getFieldMapStream")
    void successConvertBodyParamsToPost(Map<String, String> bodyParams){
        //given

        //when
        Post post = ConverterService.convert(bodyParams, Post.class);

        //then
        assertThat(post.getWriter()).isEqualTo(bodyParams.get("writer"));
        assertThat(post.getContent()).isEqualTo(bodyParams.get("content"));

        LocalDateTime written = LocalDateTime.parse(bodyParams.get("written"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        assertThat(post.getWritten()).isEqualTo(written);
    }
}