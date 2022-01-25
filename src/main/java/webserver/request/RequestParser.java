package webserver.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestParser {
    static final String BLANK_LINE = "";
    private static final Logger log = LoggerFactory.getLogger(RequestParser.class);

    public Request parse(BufferedReader br) throws IOException {
        Request request = parsingLine(br);
        parsingHeader(br, request);
        return request;
    }

    private Request parsingLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        log.info("request line {}: ", line);
        String[] requestLine = line.split(" ");
        return new Request(requestLine[1]);
    }

    private void parsingHeader(BufferedReader br, Request request) throws IOException {
        String line = br.readLine();
        while (!line.equals(BLANK_LINE)) {
            line = br.readLine();
            log.debug("request header {}: ", line);
        }
    }
}
