package model.builder;

import lombok.extern.slf4j.Slf4j;
import model.RequestHeader;
import model.ResponseHeader;
import util.Links;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public abstract class ResponseBuilder {
    public abstract ResponseHeader build(RequestHeader requestHeader) throws IOException;

    public byte[] readBody(String uri) throws IOException {
        return Files.readAllBytes(new File(Links.RETURN_BASE + uri).toPath());
    }
}
