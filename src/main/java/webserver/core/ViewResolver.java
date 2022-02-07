package webserver.core;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ResponseHandler;
import webserver.context.Json;
import webserver.context.ServletResponse;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class ViewResolver {
    private static final Logger log = LoggerFactory.getLogger(ViewResolver.class);

    private static final String resourcePath =  Paths.get(System.getProperty("user.dir"), "/src/main/resources").toString();
    private static final String staticPath =  Paths.get(resourcePath, "static").toString();
    private static final String templatesPath =  Paths.get(resourcePath, "templates").toString();

    private static final MustacheFactory mf = new DefaultMustacheFactory(new CustomMustacheResolver(new File(templatesPath)));

    private static final ByteArrayOutputStream bas = new ByteArrayOutputStream(1024);

    public static boolean resolveStaticView(OutputStream out, String path) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        String filePath =  Paths.get(staticPath, path).toString();
        File res = new File(filePath);
        if (res.exists() && res.isFile()) {
            byte[] body = Files.readAllBytes(res.toPath());
            ResponseHandler.response200HeaderWithCookie(dos, body.length, "", ResponseHandler.getMediaType(filePath));
            ResponseHandler.responseBody(dos, body);
            return true;
        }
        return false;
    }

    public static void resolveModelAndView(OutputStream out, ServletResponse servletResponse) throws IOException {
        if (servletResponse.getResponseData() instanceof Json) {
            resolveRestApiView(out, servletResponse);
            return;
        }
        resolveNoRestApiView(out, servletResponse);
    }

    public static void resolveNoRestApiView(OutputStream out, ServletResponse servletResponse) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        String path = (String) servletResponse.getResponseData();
        if (path.contains("redirect:")) {
            ResponseHandler.redirectHeader(dos, path.replace("redirect:", ""));
            return;
        }
        bas.reset();
        mf.compile(path).execute(new PrintWriter(bas, false, Charset.availableCharsets().get("UTF-8")), new Map[]{servletResponse.getModel().getData(), servletResponse.getHttpSession().getData()}).flush();
        byte[] body = bas.toByteArray();
        ResponseHandler.response200HeaderWithCookie(dos, body.length, servletResponse.getHttpSession().getCookie(), ResponseHandler.getMediaType(".html"));
        ResponseHandler.responseBody(dos, body);
    }

    public static void resolveRestApiView(OutputStream out, ServletResponse servletResponse) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = ((Json)servletResponse.getResponseData()).getJsonString().getBytes();
        ResponseHandler.response200HeaderWithCookie(dos, body.length, servletResponse.getHttpSession().getCookie(), ResponseHandler.getMediaType(".html"));
        ResponseHandler.responseBody(dos, body);
    }


}
