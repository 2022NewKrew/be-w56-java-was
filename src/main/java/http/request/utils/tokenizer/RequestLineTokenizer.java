package http.request.utils.tokenizer;

public class RequestLineTokenizer {

    private static final String REQUEST_LINE_DELIMITER = " ";

    public static String[] tokenize(String requestLine) {
        return requestLine.split(REQUEST_LINE_DELIMITER);
    }
}
