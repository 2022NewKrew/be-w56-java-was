package view;

import http.HttpStatusCode;
import util.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static util.ConstantValues.ROOT_DIRECTORY;

public class ViewStatic implements ViewRender{

    private final String url;
    private final String mimeType;
    private HttpStatusCode statusCode = HttpStatusCode.NOT_FOUND;

    public ViewStatic(String url) throws IOException {
        this.url = ROOT_DIRECTORY + url;
        if(Files.isRegularFile(Path.of(this.url))){
            this.statusCode = HttpStatusCode.SUCCESS;
        }
        this.mimeType = IOUtils.readMimeType(this.url);
    }

    @Override
    public byte[] render() throws IOException {
        return Files.readAllBytes(new File(url).toPath());
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
}
