package servlet.view;

import http.header.Cookie;
import http.message.ResponseMessage;

import java.io.IOException;

public interface View {
    ResponseMessage render(Model model, Cookie cookie) throws IOException;
}