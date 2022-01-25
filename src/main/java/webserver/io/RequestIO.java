package webserver.io;

public class RequestIO {
    public static boolean checkStringIsEmpty(String str) {
        return str == null || "".equals(str.trim());
    }
}
