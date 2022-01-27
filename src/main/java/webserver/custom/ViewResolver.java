package webserver.custom;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.context.Model;

import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

public class ViewResolver {
    private static final Logger log = LoggerFactory.getLogger(ViewResolver.class);

    public static byte[] resolveModelAndView(String path, Model model) throws IOException {
        String curPath = System.getProperty("user.dir");
        String targetFilePath = Paths.get(curPath, "/src/main/resources/", path).toString();
        log.debug("resource path: " + targetFilePath);

        FileReader reader = new FileReader(targetFilePath);

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(reader,"");

        ByteArrayOutputStream bas = new ByteArrayOutputStream(1024);
        mustache.execute(new PrintWriter(bas), model).flush();

        return bas.toByteArray();
    }

}
