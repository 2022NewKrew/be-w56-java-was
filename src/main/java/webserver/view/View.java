package webserver.view;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.common.FileLocation;
import webserver.common.Status;
import webserver.controller.response.HttpResponse;

public class View {

    public static void render(OutputStream out, HttpResponse httpResponse) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        BiConsumer<DataOutputStream, HttpResponse> renderer = RenderType.findRenderer(httpResponse);
        renderer.accept(dos, httpResponse);
    }
}
