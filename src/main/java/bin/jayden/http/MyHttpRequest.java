package bin.jayden.http;

import bin.jayden.util.Constants;
import bin.jayden.util.HttpRequestUtils;
import bin.jayden.util.IOUtils;
import com.google.common.base.Strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MyHttpRequest {
    private final static String DEFAULT_PAGE = "/index.html";
    private final String method;
    private final String path;
    private final MyHttpResponseHeader header;
    private final Map<String, String> params;
    private final Map<String, String> cookies;
    private final Mime mime;

    private MyHttpRequest(String method, String path, Map<String, String> params, MyHttpResponseHeader header, Map<String, String> cookies, Mime mime) {
        this.method = method;
        if (path.isEmpty() || path.equals("/"))
            this.path = DEFAULT_PAGE;
        else
            this.path = path;
        this.params = params;
        this.header = header;
        this.cookies = cookies;
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

    public MyHttpResponseHeader getHeader() {
        return header;
    }

    public Mime getMime() {
        return mime;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public static class Builder {
        public MyHttpRequest build(InputStream inputStream) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            MyHttpResponseHeader headers = new MyHttpResponseHeader();
            String line = reader.readLine();
            String[] tokens = line.split(" ");
            String method = tokens[0].toUpperCase();
            String url = tokens[1];
            String[] urlTokens = url.split("\\?", 2);
            String path = urlTokens[0];
            Mime mime = Mime.getMineByPath(path.toLowerCase());
            Map<String, String> cookies = new HashMap<>();
            while (true) {
                line = reader.readLine();
                if (Strings.isNullOrEmpty(line))
                    break;
                HttpRequestUtils.Pair header = HttpRequestUtils.parseHeader(line);
                if (header.getKey().equals("Cookie"))
                    cookies = HttpRequestUtils.parseCookies(header.getValue());
                headers.addHeader(header);
            }
            Map<String, String> params = null;
            if (method.equals(Constants.HTTP_METHOD_GET)) {
                if (urlTokens.length > 1) {
                    params = HttpRequestUtils.parseQueryString(urlTokens[1]);
                    params.replaceAll((k, v) -> URLDecoder.decode(v, StandardCharsets.UTF_8));
                }
            } else if (method.equals(Constants.HTTP_METHOD_POST)) {

                line = IOUtils.readData(reader, Integer.parseInt(headers.getHeaderValue("Content-Length")));
                params = HttpRequestUtils.parseQueryString(line);
                params.replaceAll((k, v) -> URLDecoder.decode(v, StandardCharsets.UTF_8));
            }

            return new MyHttpRequest(method, path, params, headers, cookies, mime);
        }
    }
}
