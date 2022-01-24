package webserver.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.BadRequestException;
import webserver.exception.InternalServerErrorException;
import webserver.exception.ResourceNotFoundException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class DocumentRoot {
    public static final String ROOT_DIRECTORY = "webapp";
    public static final String DEFAULT_PATH = "/index.html";

    public byte[] readFileByPath(URI requestUri) {
        byte[] body = null;
        try {
            URL url = resolveURL(requestUri);
            body = Files.readAllBytes(Path.of(url.toURI()));
        } catch (IOException e) {
            throw new InternalServerErrorException(e.getClass().getName(), e);
        } catch (URISyntaxException e) {
            throw new BadRequestException(e.getClass().getName(), e);
        }  catch (NullPointerException e) {
            throw new ResourceNotFoundException(e.getClass().getName(), e);
        }
        return body;
    }

    private URL resolveURL(URI uri) {
        String path = uri.getPath();
        if(path.equals("/")) {
            path = DEFAULT_PATH;
        }
        return ClassLoader.getSystemResource(ROOT_DIRECTORY + path);
    }
}
