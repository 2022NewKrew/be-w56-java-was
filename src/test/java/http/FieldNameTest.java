package http;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FieldNameTest {
    @Test
    void equals() {
        FieldName test1 = new FieldName("Content-Type");
        FieldName test2 = new FieldName("Content-Type");
        FieldName test3 = new FieldName("content-type");

        assertThat(test1).isEqualTo(test2);
        assertThat(test1).isNotEqualTo(test3);
    }
}
