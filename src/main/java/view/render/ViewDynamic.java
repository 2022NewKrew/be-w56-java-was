package view.render;

import http.HttpStatusCode;
import model.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;
import util.TemplateEngine;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static util.ConstantValues.ROOT_DIRECTORY;


public class ViewDynamic implements ViewRender{

    private static final Logger log = LoggerFactory.getLogger(ViewDynamic.class);

    private final ModelAndView mv;
    private final String url;
    private final String mimeType;
    private HttpStatusCode statusCode = HttpStatusCode.NOT_FOUND;
    private final StringBuilder sb = new StringBuilder();

    public ViewDynamic(ModelAndView mv) throws IOException {
        this.mv = mv;
        this.url = ROOT_DIRECTORY + this.mv.getViewName();
        if(Files.isRegularFile(Path.of(this.url))){
            this.statusCode = HttpStatusCode.SUCCESS;
        }
        this.mimeType = IOUtils.readMimeType(this.url);
    }

    @Override
    public byte[] render() throws IOException {
        File file = new File(url);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        log.info("[ViewDynamic] Render: " + url);
        TemplateEngine templateEngine = new TemplateEngine(br, mv);
        sb.append(templateEngine.read());
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String getMimeType() {
        return this.mimeType;
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return this.statusCode;
    }
}
