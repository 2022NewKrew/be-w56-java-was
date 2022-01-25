package http;

import com.google.common.base.Strings;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyHttpRequest {
    private final static String DEFAULT_PAGE = "/index.html";
    private final String method;
    private final String path;
    private final List<Pair> header;
    private final Map<String, String> params;
    private final Mime mime;

    private MyHttpRequest(String method, String path, Map<String, String> params, List<Pair> header, Mime mime) {
        this.method = method.toUpperCase();
        if (path.isEmpty() || path.equals("/"))
            this.path = DEFAULT_PAGE;
        else
            this.path = path;
        this.params = params;
        this.header = header;
        this.mime = mime;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public List<Pair> getHeader() {
        return header;
    }

    public Mime getMime() {
        return mime;
    }

    public static class Builder {
        public MyHttpRequest build(InputStream inputStream) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            List<Pair> headers = new ArrayList<>();
            String line = reader.readLine();
            String[] tokens = line.split(" ");
            String method = tokens[0];
            String url = tokens[1];
            String[] urlTokens = url.split("\\?", 2);
            String path = urlTokens[0];
            Mime mime = Mime.getMineByPath(path.toLowerCase());
            line = reader.readLine();
            while (!Strings.isNullOrEmpty(line)) {
                Pair header = HttpRequestUtils.parseHeader(line);
                headers.add(header);
                line = reader.readLine();
            }
            Map<String, String> params = null;
            if (urlTokens.length > 1) {
                params = HttpRequestUtils.parseQueryString(urlTokens[1]);
                params.replaceAll((k, v) -> URLDecoder.decode(v, StandardCharsets.UTF_8));
            }

            return new MyHttpRequest(method, path, params, headers, mime);
        }
    }
}
