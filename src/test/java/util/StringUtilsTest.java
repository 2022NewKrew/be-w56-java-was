package util;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void parseJsonString() {
        String s = "\"password\":\"secret\",\"url\":\"jdbc:mysql://localhost:3306/wasdb\",\"user\":\"root\"";
        Map<String, String> data = StringUtils.parseJsonString(s);
        assertEquals(data.get("password"), "secret");
        assertEquals(data.get("url"), "jdbc:mysql://localhost:3306/wasdb");
        assertEquals(data.get("user"), "root");
    }
}
