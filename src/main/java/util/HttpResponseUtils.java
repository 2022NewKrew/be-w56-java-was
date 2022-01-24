package util;

public class HttpResponseUtils {

    /**
     * @param 파일 확장자
     *
     * @return content-type
     */
    public static String contentTypeOf(String extension) {
        if ("css".equals(extension))
            return "text/css";
        else if ("javascript".equals(extension))
            return "text/javascript";

        return "text/html";
     }
}
