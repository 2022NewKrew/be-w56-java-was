package com.kakao.util;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtils {
    /**
     * @param br            해당 Reader는 Request Body를 시작하는 시점이어야 한다.
     * @param contentLength Request Header의 Content-Length 값이다.
     * @return 주어진 Reader를 contentLength 만큼 읽은 String
     * @throws IOException 주어진 Reader에 대해 contentLength 길이만큼 읽기에 실패했을 경우
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        int read = br.read(body, 0, contentLength);
        if (read != contentLength) {
            throw new IOException(String.format("contentLength[%s] and read bytes[%s] not matched",
                    contentLength, read));
        }
        return String.copyValueOf(body);
    }
}
