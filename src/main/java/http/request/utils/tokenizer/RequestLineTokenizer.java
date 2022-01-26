package http.request.utils.tokenizer;

public class RequestLineTokenizer {

    public static String[] tokenize(String requestLine) {
        return requestLine.split(" ");
    }
}
