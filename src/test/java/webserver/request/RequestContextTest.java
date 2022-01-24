package webserver.request;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestContextTest {

    @Test
    void test() {
        String[] result = StringUtils.split("split: sleep", ": ");
        for(String str: result) {
            System.out.println(str);
        }
    }
}
