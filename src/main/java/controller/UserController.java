package controller;

import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.HttpRequest;
import request.RequestBody;
import response.HttpResponse.HttpResponseBuilder;
import response.HttpStatusCode;
import response.ResHeader;
import response.ResponseHeader;
import response.ResponseHeader.ResponseHeaderBuilder;
import util.IOUtils;
import util.UrlUtils;
import webserver.UrlMapper;

public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private static final UrlMapper URL_MAPPER = UrlMapper.getInstance();

    private UserController() {}

    public static void register() {

        URL_MAPPER.put(
            "/user/form.html",
            "GET",
            (HttpRequest httpRequest) -> {
                byte[] body = IOUtils.readFile("./webapp/user/form.html");
                ResponseHeader responseHeader = new ResponseHeaderBuilder()
                        .set(ResHeader.CONTENT_LENGTH, "" + body.length)
                        .build();

                return new HttpResponseBuilder(HttpStatusCode.OK)
                        .setHeader(responseHeader)
                        .setBody(body)
                        .build();
            }
        );

        URL_MAPPER.put(
            "/user/create",
            "POST",
            (HttpRequest httpRequest) -> {
                RequestBody requestBody = httpRequest.getRequestBody();
                String body = new String(requestBody.getBody());
                body = UrlUtils.decode(body);
                Map<String, String> bodyData = IOUtils.getBodyData(body);

                try {
                    User user = new User(bodyData.get("userId"), bodyData.get("password"),
                            bodyData.get("name"), bodyData.get("email"));
                    LOG.info("Success SignIn - {}", user);
                    ResponseHeader responseHeader = new ResponseHeaderBuilder()
                            .set(ResHeader.LOCATION, "/")
                            .build();

                    return new HttpResponseBuilder(HttpStatusCode.FOUND)
                            .setHeader(responseHeader)
                            .build();

                } catch (NullPointerException e) {
                    LOG.error(e.getMessage());
                    return new HttpResponseBuilder(HttpStatusCode.NOT_ACCEPTABLE).build();
                }
            }
        );
    }
}
