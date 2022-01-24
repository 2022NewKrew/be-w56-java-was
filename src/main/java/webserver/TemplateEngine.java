package webserver;

import webserver.http.HttpClientErrorException;
import webserver.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TemplateEngine {
    private static TemplateEngine templateEngine;
    private TemplateEngine(){}

    public static TemplateEngine getInstance(){
        if (templateEngine == null){
            templateEngine = new TemplateEngine();
        }

        return templateEngine;
    }

    public byte[] render(String url){
        try{
            return Files.readAllBytes(new File("./webapp" + url).toPath());
        }catch(IOException e){
            e.printStackTrace();
            throw new HttpClientErrorException(HttpStatus.NotFound, "해당 페이지를 찾을 수 없습니다.");
        }
    }
}
