package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

public class Header {

    public static Header of(List<String> headerLineList) {
        Map<String, String> header = new HashMap<>();

        for (String headerLine : headerLineList) {
            Pair parseHeaderResult = HttpRequestUtils.parseHeader(headerLine);
            header.put(parseHeaderResult.getKey(), parseHeaderResult.getValue());
        }

        return new Header(header);
    }

    private final Map<String, String> header;

    public Header(Map<String, String> header) {
        this.header = Collections.unmodifiableMap(header);
    }

    public String get(String key) {
        return header.get(key);
    }

    public List<String> messageList() {
        List<String> messageList = new ArrayList<>();
        for (Entry<String, String> entry : header.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            messageList.add(key + ": " + value);
        }
        return messageList;
    }

    @Override
    public String toString() {
        return "Header{" +
                "header=" + header +
                '}';
    }
}
