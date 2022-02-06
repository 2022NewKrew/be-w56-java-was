package http.request.utils.tokenizer;

public class UriTokenizer {
    private static final String URI_QUERIES_DELIMITER = "\\?";

    public static String[] tokenize(String uri) {
        String[] token = uri.split(URI_QUERIES_DELIMITER);

        if (token.length < 2) {
            return new String[]{uri, ""};
        }

        return token;
    }
}
