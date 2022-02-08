package model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("BaseTime 테스트")
class BaseTimeTest {

    @DisplayName("올바른 파라미터로 BaseTime을 생성했을 때 예외를 던지지 않는다.")
    @Test
    void constructor1() {
        //give
        LocalDateTime createTime = LocalDateTime.now();
        LocalDateTime modifiedTime = LocalDateTime.now();

        //when
        //then
        assertThatCode(() -> new BaseTime(createTime, modifiedTime)).doesNotThrowAnyException();
    }

    @DisplayName("createTime이 null 일때 IllegalArgumentException을 던진다.")
    @Test
    void illegalConstructor1() {
        //give
        LocalDateTime createTime = null;
        LocalDateTime modifiedTime = LocalDateTime.now();

        //when
        //then
        assertThatThrownBy(() -> new BaseTime(createTime, modifiedTime)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("ModifiedTime이 null 일때 IllegalArgumentException을 던진다.")
    @Test
    void illegalConstructor2() {
        //give
        LocalDateTime createTime = LocalDateTime.now();
        LocalDateTime modifiedTime = null;

        //when
        //then
        assertThatThrownBy(() -> new BaseTime(createTime, modifiedTime)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("BaseTime modifiedTime setter 테스트")
    @Test
    void getCreateTime() {
        LocalDateTime now = LocalDateTime.now();

        BaseTime baseTime = new BaseTime(now, now);

        assertThat(baseTime.getModifiedTime()).isEqualTo(now);

        LocalDateTime modifiedDate = LocalDateTime.of(1998, 5, 2, 12, 0);
        baseTime.setModifiedTime(modifiedDate);

        assertThat(baseTime.getModifiedTime()).isEqualTo(modifiedDate);
    }
}
