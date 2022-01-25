package webserver;

import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Header {

    Map<String, String> attributes = new HashMap<>();

    public void save(BufferedReader br) throws IOException {
        String line = br.readLine();
        while (!line.equals("")) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            attributes.put(pair.getKey(), pair.getValue());
            line = br.readLine();
        }
    }

    public String getAttribute(String key){
        return attributes.get(key);
    }
}
