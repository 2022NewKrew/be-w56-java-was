package model.builder;

import lombok.extern.slf4j.Slf4j;
import model.RequestHeader;
import model.ResponseHeader;
import util.Constant;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public abstract class ResponseBuilder {
    public abstract ResponseHeader build(RequestHeader requestHeader);

    public byte[] readBody(String uri) {
        try {
            return Files.readAllBytes(new File(Constant.RETURN_BASE + uri).toPath());
        } catch (IOException e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
        // TODO Error 세분화
        throw new IllegalStateException("READ BODY ERROR");
    }
}
