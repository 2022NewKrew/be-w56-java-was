package webserver.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpMethod;
import webserver.request.HttpRequest;
import webserver.request.HttpRequestHeader;
import webserver.request.HttpRequestStartLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RequestParser {
    private static final Logger log = LoggerFactory.getLogger(RequestParser.class);

    public HttpRequest doParse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        String line = br.readLine();

        String startLine = line;
        List<String> header = new ArrayList<>();

        while (true) {
            line = br.readLine();
            if (line.equals("")) {
                break;
            }
            header.add(line);
        }

        HttpRequestStartLine httpRequestStartLine = doParseStartLine(startLine);
        HttpRequestHeader httpRequestHeader = doParseHeader(header);

        return new HttpRequest(httpRequestStartLine, httpRequestHeader);
    }

    public HttpRequestStartLine doParseStartLine(String startLine) {
        String[] split = startLine.split(" ");

        String method = split[0];
        String targetUri = split[1];
        String version = split[2];

        log.debug("target uri: {}", targetUri);
        return new HttpRequestStartLine(HttpMethod.valueOf(method), targetUri, version);
    }

    public static HttpRequestHeader doParseHeader(List<String> header) {
        HttpRequestHeader httpRequestHeader = new HttpRequestHeader();

        for (String message : header) {
            //log.debug("header message: {}", message);
            String[] split = message.split(": ");

            String key = split[0];
            String value = split[1];

            switch (key) {
                case "Host":
                    httpRequestHeader.setHost(value);
                    break;
                case "Accept":
                    httpRequestHeader.setAccept(value);
                    break;
                case "Connection":
                    httpRequestHeader.setConnection(value);
                    break;
                case "Content-Type":
                    httpRequestHeader.setContentType(value);
                    break;
                case "Content-Length":
                    httpRequestHeader.setContentLength(Long.parseLong(value));
                default:
                    break;
            }
        }

        return httpRequestHeader;
    }
}
