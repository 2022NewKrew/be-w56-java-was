package http.response;

import http.HttpStatusCode;
import model.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static util.ConstantValues.REDIRECT_COMMAND;
import static util.ConstantValues.REDIRECT_IDX;

public class Response {
    private static final String ROOT_URL = "http://localhost:8080";
    private static final String ROOT_DIRECTORY = "./webapp";
    private static final Logger log = LoggerFactory.getLogger(Response.class);

    private final DataOutputStream dos;
    private final ModelAndView mv;
    private HttpStatusCode statusCode = HttpStatusCode.NOT_FOUND;

    public Response(DataOutputStream dos, ModelAndView mv){
        this.dos = dos;
        this.mv = mv;
    }

    private void generateHttpStatus(){
        if(mv.getViewName().indexOf(REDIRECT_COMMAND) == REDIRECT_IDX){
            statusCode = HttpStatusCode.REDIRECT;
        }
        if(Files.isRegularFile(Path.of(ROOT_DIRECTORY + mv.getViewName()))){
            statusCode = HttpStatusCode.SUCCESS;
        }
    }


    public void write() throws IOException {
        generateHttpStatus();
        log.info("HTTP STATUS : " + statusCode.getStatusCode() + ", " + statusCode.getMessage());
        log.info(("View : " + mv.getViewName()));
        statusCode.response(dos, mv);
    }
}
