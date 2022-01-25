package webserver.file;

import webserver.exception.InternalServerErrorException;
import webserver.exception.ResourceNotFoundException;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class DocumentRoot {
    public static final String ROOT_DIRECTORY = "webapp";
    public static final String DEFAULT_PATH = "/index.html";

    public boolean existsFile(String path) {
        URL url = getResourceUrlByPath(path);
        return url != null;
    }

    public byte[] readFileByPath(String path) {
        byte[] body = null;
        try {
            String resolvedPath = resolvePath(path);
            body = Files.readAllBytes(Path.of(resolvedPath));
        } catch (IOException e) {
            throw new InternalServerErrorException(e.getClass().getName(), e);
        } catch (NullPointerException e) {
            throw new ResourceNotFoundException(e.getClass().getName(), e);
        }
        return body;
    }

    private String resolvePath(String path) {
        if(path.equals("/")) {
            path = DEFAULT_PATH;
        }
        return getResourceUrlByPath(path).getPath();
    }

    private URL getResourceUrlByPath(String path) {
        return ClassLoader.getSystemResource(ROOT_DIRECTORY + path);
    }
}
