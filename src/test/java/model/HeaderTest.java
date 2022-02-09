package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HeaderTest {

    @Test
    @DisplayName("[성공] Header 객체를 생성한다")
    void of() {
        List<String> headerLineList = List.of("key1: value1", "key2: value2");

        Header header = Header.of(headerLineList);
    }

    @Test
    @DisplayName("[성공] Header 객체를 생성한다 - 헤더가 없는 경우")
    void of_By_EmptyHeader() {
        List<String> headerLineList = List.of();

        Header header = Header.of(headerLineList);
    }

    @Test
    @DisplayName("[성공] Header에서 key를 가져온다")
    void get() {
        HttpHeader key1 = HttpHeader.ACCEPT;
        String value1 = "value1";
        HttpHeader key2 = HttpHeader.CONTENT_TYPE;
        String value2 = "value2";
        List<String> headerLineList = List.of(key1 + ": value1", key2 + ": value2");
        Header header = Header.of(headerLineList);

        Assertions.assertEquals(header.get(key1), value1);
        Assertions.assertEquals(header.get(key2), value2);
    }

    @Test
    @DisplayName("[성공] Header를 String 리스트로 들고온다")
    void messageList() {
        HttpHeader key1 = HttpHeader.ACCEPT;
        String value1 = "value1";
        HttpHeader key2 = HttpHeader.CONTENT_TYPE;
        String value2 = "value2";
        List<String> headerLineList = List.of(key1 + ": value1", key2 + ": value2");
        List<String> messageList = List.of(key1 + ": value1", key2 + ": value2");

        Header header = Header.of(headerLineList);

        Assertions.assertEquals(header.messageList(), messageList);
    }
}
