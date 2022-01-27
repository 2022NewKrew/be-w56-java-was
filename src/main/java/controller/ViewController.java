package controller;

import controller.request.Request;
import controller.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by melodist
 * Date: 2022-01-25 025
 * Time: 오후 7:01
 */
public class ViewController implements WebController {
    private static final Logger log = LoggerFactory.getLogger(ViewController.class);

    @Override
    public Response process(Request request) {
        Response response = null;

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/" + request.getContentType() + ";charset=utf-8");
        try {
            byte[] body = readBody(request.getPath());
            headers.put("Content-Length", String.valueOf(body.length));

            response = new Response.Builder()
                    .ok()
                    .headers(headers)
                    .body(body)
                    .build();
        }
        catch (IOException e) {
            log.error(e.getMessage());
        }
        return response;
    }

    private byte[] readBody(String path) throws IOException{
        return Files.readAllBytes(new File("./webapp" + path).toPath());
    }
}
