package webserver.http.response;

import util.constant.Parser;
import util.constant.Route;
import webserver.http.request.HttpRequest;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static util.HttpRequestUtils.urlToFile;

public class HttpResponseUtils {

    public static HttpResponse redirectTo(OutputStream out) {
        return new HttpResponse.Builder(out)
                .setHttpStatus(HttpStatus._302)
                .setRedirect(Route.INDEX.getPath())
                .build();
    }

    public static HttpResponse redirectTo(OutputStream out, String url) {
        return new HttpResponse.Builder(out)
                .setHttpStatus(HttpStatus._302)
                .setRedirect(url)
                .build();
    }

    public static HttpResponse redirectTo(OutputStream out, String url, String cookie) {
        return new HttpResponse.Builder(out)
                .setHttpStatus(HttpStatus._302)
                .setCookie(cookie)
                .setRedirect(url)
                .build();
    }

    public static HttpResponse ok(OutputStream out, HttpRequest request) throws IOException {
        Path target = urlToFile(request.getUrl());
        String[] tokens = target.toString().split(Parser.DOT);
        ContentType contentType = ContentType.of(tokens[tokens.length - 1].toUpperCase());
        File file = target.toFile();
        byte[] body = Files.readAllBytes(file.toPath());

        return new HttpResponse.Builder(out)
                .setBody(body)
                .setHttpStatus(HttpStatus._200)
                .setContentType(contentType.getExtension())
                .setContentLength(body.length)
                .setRedirect(Route.BASE.getPath() + Route.INDEX.getPath())
                .build();
    }

    public static HttpResponse ok(OutputStream out, HttpRequest request, StringBuilder sb) throws IOException {
        Path target = urlToFile(request.getUrl());
        String[] tokens = target.toString().split(Parser.DOT);
        ContentType contentType = ContentType.of(tokens[tokens.length - 1].toUpperCase());
        File file = target.toFile();
        String fileData = new String(Files.readAllBytes(file.toPath()));
        fileData = fileData.replace("%user_list%", URLDecoder.decode(sb.toString(), StandardCharsets.UTF_8));
        byte[] body = fileData.getBytes(StandardCharsets.UTF_8);

        return new HttpResponse.Builder(out)
                .setBody(body)
                .setHttpStatus(HttpStatus._200)
                .setContentType(contentType.getExtension())
                .setContentLength(body.length)
                .setRedirect(Route.BASE.getPath() + Route.INDEX.getPath())
                .build();
    }

    public static HttpResponse notFound(OutputStream out) {
        return new HttpResponse.Builder(out)
                .setHttpStatus(HttpStatus._404)
                .setRedirect(Route.INDEX.getPath())
                .build();
    }
}
