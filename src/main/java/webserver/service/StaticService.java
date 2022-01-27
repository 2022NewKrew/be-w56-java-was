package webserver.service;

import java.io.File;

import webserver.common.FileLocation;
import webserver.common.Status;
import webserver.controller.request.HttpRequest;
import webserver.controller.response.HttpResponse;

public class StaticService {

    public HttpResponse getStatic(HttpRequest httpRequest){
        // 정적 파일이 있으면 정적 파일 제공
        if (validateStaticServe(httpRequest)){
            return HttpResponse.of(httpRequest.getPath(), Status.OK);
        }
        // 정적 파일이 없으면 404 에러 제공
        return HttpResponse.of(FileLocation.NONE.path, Status.NOT_FOUND);
    }

    private boolean validateStaticServe(HttpRequest httpRequest){
        File file = new File(FileLocation.FILE_DIRECTORY.path + httpRequest.getPath());
        return file.isFile();
    }
}
