package webserver.core;

import org.apache.tika.Tika;
import webserver.core.http.HttpClientErrorException;
import webserver.core.http.request.Header;
import webserver.core.http.request.HttpRequest;
import webserver.core.http.response.HttpResponse;
import webserver.core.http.response.HttpResponseBuilder;
import webserver.core.http.response.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class StaticResourceManager {

    /*
    * ./webapp 하위의 모든 폴더 경로를 가져옵니다.
    */
    private static final Set<String> staticResource = new HashSet<>()
    {{
        File file = new File("./webapp");
        Queue<File> queue = new LinkedList<>(Arrays.asList(Objects.requireNonNull(file.listFiles())));
        while(!queue.isEmpty()){
            File f = queue.poll();
            if (f.isDirectory()){
                queue.addAll(Arrays.asList(Objects.requireNonNull(f.listFiles())));
                continue;
            }
            add(f.getPath().substring(8));
        }
    }};


    public static boolean has(String url){
        return staticResource.contains(url);
    }

    public static HttpResponse getResource(HttpRequest request){
        File file = new File("./webapp" + request.getUrl());
        HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder();
        Tika tika = new Tika();
        try{
            return httpResponseBuilder
                    .setStatus(HttpStatus.Ok)
                    .addHeaderValue("Content-Type", tika.detect(file))
                    .setBody(Files.readAllBytes(file.toPath()))
                    .build();

        }catch(IOException e){
            e.printStackTrace();
            throw new HttpClientErrorException(HttpStatus.NotFound, "해당 리소스를 찾을 수 없습니다.");
        }
    }
}
