package controller;

import model.Request;
import model.Response;
import util.FilesUtils;

import java.io.IOException;

public class FrontController extends AbstractController {
    private static final String BASIC_URL = "/";
    private static final String INDEX_URL = "/index.html";

    @Override
    protected void doGet(Request request, Response response) throws IOException {
        byte[] responseBody = getResponseBody(request);
        response.set200Header(request, responseBody);
    }

    private byte[] getResponseBody(Request request) throws IOException {
        if (request.isEqualUrl(BASIC_URL)) {
            return FilesUtils.fileToByte(INDEX_URL);
        }

        return FilesUtils.fileToByte(request.getUrl());
    }

    @Override
    protected void doPost(Request request, Response response) throws IOException {

    }
}
