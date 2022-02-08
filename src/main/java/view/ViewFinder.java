package view;

import model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

public class ViewFinder {

    private static final Logger log = LoggerFactory.getLogger(ViewFinder.class);

    public static byte[] find(String path, Model model){
        try {
            return staticViewFind(path);
        }catch (NoSuchFileException ex) {
            try {
                return dynamicViewFind(path, model);
            }catch (Exception ex2) {
                log.error(ex2.getMessage());
                for(StackTraceElement element : ex2.getStackTrace()) {
                    log.error(element.toString());
                }
                return "".getBytes();
            }
        }catch (IOException ex) {
            log.error(ex.getMessage());
            for(StackTraceElement element : ex.getStackTrace()) {
                log.error(element.toString());
            }
            return "".getBytes();
        }
    }

    private static byte[] staticViewFind(String path) throws IOException {
        return Files.readAllBytes(new File("./webapp" + path).toPath());
    }

    private static byte[] dynamicViewFind(String path, Model model)
            throws InvocationTargetException, IllegalAccessException {
        for (Method method : ViewCreator.class.getMethods()) {
            if (method.getName().startsWith(path.substring(1))) {
                method.invoke(ViewCreator.class, model, path);
            }
        }
        return ViewMapper.getView(path).getBytes();
    }
}
