package model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("BaseTime 테스트")
class BaseTimeTest {

    @DisplayName("BaseTime 테스트")
    @Test
    void getCreateTime() {
        LocalDateTime now = LocalDateTime.now();

        BaseTime baseTime = new BaseTime(now, now);

        assertThat(baseTime.getCreateTime()).isEqualTo(now);
        assertThat(baseTime.getModifiedTime()).isEqualTo(now);

        LocalDateTime modifiedDate = LocalDateTime.of(1998, 5, 2, 12, 0);
        baseTime.setModifiedTime(modifiedDate);

        assertThat(baseTime.getModifiedTime()).isEqualTo(modifiedDate);
    }
}
