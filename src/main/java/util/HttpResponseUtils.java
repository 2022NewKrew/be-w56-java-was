package util;

public class HttpResponseUtils {

    /**
     * @param 파일 확장자
     *
     * @return content-type
     */
    public static String contentTypeOf(String url) {
        if (url == null)
            return "text/plain";

        if (url.endsWith(".css"))
            return "text/css";
        else if (url.endsWith(".js"))
            return "text/javascript";

        return "text/html";
     }
}
