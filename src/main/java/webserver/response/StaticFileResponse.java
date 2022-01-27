package webserver.response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class StaticFileResponse extends Response {

    StaticFileResponse(String mimeType, File file) throws IOException {
        super(StatusCode.OK);
        setContents(mimeType, Files.readAllBytes(file.toPath()));
    }
}
