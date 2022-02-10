package webserver;

public class ViewResolver {

    private static final String PREFIX = "./webapp/";
    private static final String SUFFIX = ".html";

    private ViewResolver() {}

    public static String getStaticFilePath(String staticFileName) {
        return PREFIX + staticFileName + SUFFIX;
    }
}
