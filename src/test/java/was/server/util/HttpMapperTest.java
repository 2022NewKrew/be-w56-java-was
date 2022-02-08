package was.server.util;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

class HttpMapperTest {

    @Test
    void toHttpRequest() {
        final HttpMapper httpMapper = new HttpMapper();

        byte[] bytes = ("GET /users/login?test=123&abc=q2f3v341 HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Content-Length: 78\n" +
                "\n" +
                "email=test%40test.com&password=test&_csrf=efbe2748-87d8-4a89-b5e9-06bdd2e881fd").getBytes(StandardCharsets.UTF_8);

        int pre = 0;
        int cur = 0;
        while (cur < bytes.length) {
            if (bytes[cur] == ' ') {
                System.out.println(new String(bytes, pre, cur - pre));
                cur++;
                pre = cur;
                continue;
            } else if (bytes[cur] == '\n') {
                int length = cur - pre;
                if (bytes[cur - 1] == '\r') {
                    length--;
                }

                System.out.println(new String(bytes, pre, length));
                cur++;
                pre = cur;
                break;
            }
            cur++;
        }

        while (cur < bytes.length) {
            //key
            if (bytes[cur] == ':' && bytes[cur + 1] == ' ') {
                System.out.println(new String(bytes, pre, cur - pre));
                cur += 2;
                pre = cur;
                continue;
                //value
            } else if (bytes[cur] == '\n') {
                int length = cur - pre;
                if (bytes[cur - 1] == '\r') {
                    length--;
                }

                System.out.println(new String(bytes, pre, length));
                if (bytes[cur + 1] == '\n') {
                    cur += 2;
                    pre = cur;
                    break;
                }

                if (bytes[cur + 2] == '\n') {
                    cur += 3;
                    pre = cur;
                    break;
                }

                cur++;
                pre = cur;
                continue;
            }
            cur++;
        }

        System.out.println(new String(bytes, pre, 78));
    }
}
