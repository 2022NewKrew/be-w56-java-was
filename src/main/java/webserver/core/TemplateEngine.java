package webserver.core;

import webserver.core.http.HttpClientErrorException;
import webserver.core.http.response.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TemplateEngine {

    public static byte[] render(String url){
        try{
            return Files.readAllBytes(new File("./webapp" + url).toPath());
        }catch(IOException e){
            e.printStackTrace();
            throw new HttpClientErrorException(HttpStatus.NotFound, "해당 페이지를 찾을 수 없습니다.");
        }
    }
}
