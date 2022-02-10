package model.builder;

import dynamic.DynamicModel;
import lombok.extern.slf4j.Slf4j;
import model.RequestHeader;
import model.HtmlResponse;
import util.Links;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

@Slf4j
public abstract class ResponseBuilder {
    protected DynamicModel model = new DynamicModel();

    public abstract HtmlResponse build(RequestHeader requestHeader) throws IOException, SQLException;

    public byte[] readBody(String uri) throws IOException {
        return Files.readAllBytes(new File(Links.RETURN_BASE + uri).toPath());
    }
}
