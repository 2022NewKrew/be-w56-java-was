package view;

import http.HttpStatusCode;

import java.io.IOException;

public interface View {
    HttpStatusCode getStatusCode();
}
