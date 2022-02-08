package servlet.view;

import http.message.ResponseMessage;

public interface View {
    ResponseMessage render();
}