package bin.jayden.http;

import bin.jayden.util.Constants;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class MyHttpSessionFactory {
    private static final Map<String, MyHttpSession> sessionMap = new HashMap<>();
    private static final SecureRandom secureRandom = new SecureRandom();

    public static MyHttpSession getSession(String sessionId, MyHttpResponse.Builder responseBuilder) {
        MyHttpSession session = sessionMap.get(sessionId);
        if (session == null) {
            session = new MyHttpSession();

            byte[] secureBytes = new byte[16];
            secureRandom.nextBytes(secureBytes);
            String newSessionId = bytesToHex(secureBytes);
            sessionMap.put(newSessionId, session);
            responseBuilder.addHeader("Set-Cookie", Constants.COOKIE_SESSION_KEY + "=" + newSessionId + "; Path=/");
        }
        return session;
    }

    private static String bytesToHex(byte[] bytes) {
        char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
