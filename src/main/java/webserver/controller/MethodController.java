package webserver.controller;

import java.io.IOException;

public interface MethodController {
    void service() throws IOException;
}
