package model;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArticleTest {

    @DisplayName("올바른 파라미터로 Article을 생성했을 때 예외를 던지지 않는다.")
    @Test
    void constructor() {
        //give
        ObjectId id = new ObjectId();
        String author = "author";
        String content = "content";

        //when
        //then
        assertThatCode(() -> new Article(id, author, content)).doesNotThrowAnyException();
    }

    @DisplayName("id가 null 일때 IllegalArgumentException을 던진다.")
    @Test
    void illegalConstructor1() {
        //give
        ObjectId id = null;
        String author = "author";
        String content = "content";

        //when
        //then
        assertThatThrownBy(() -> new Article(id, author, content)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("author가 null 일때 IllegalArgumentException을 던진다.")
    @Test
    void illegalConstructor2() {
        //give
        ObjectId id = new ObjectId();
        String author = null;
        String content = "content";

        //when
        //then
        assertThatThrownBy(() -> new Article(id, author, content)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("content가 null 일때 IllegalArgumentException을 던진다.")
    @Test
    void illegalConstructor3() {
        //give
        ObjectId id = new ObjectId();
        String author = "author";
        String content = null;

        //when
        //then
        assertThatThrownBy(() -> new Article(id, author, content)).isInstanceOf(
                IllegalArgumentException.class);
    }
}
