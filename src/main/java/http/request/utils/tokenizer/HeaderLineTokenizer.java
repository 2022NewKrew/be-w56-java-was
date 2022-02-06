package http.request.utils.tokenizer;

public class HeaderLineTokenizer {
    private static final String HEADER_DELIMITER = ": ";

    public static String[] tokenize(String headerLine) {
        String[] token = headerLine.split(HEADER_DELIMITER);

        if (token.length < 2) {
            return new String[]{headerLine, ""};
        }

        return token;
    }
}
