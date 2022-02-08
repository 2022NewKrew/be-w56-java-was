package webserver.method;

import http.HttpMethod;
import http.HttpRequest;
import webserver.HandlerMapping;
import webserver.RequestMappingInfo;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StaticFileHandlerMapping implements HandlerMapping {
    private final static String STATIC_PATH = "webapp";
    private final Map<RequestMappingInfo, StaticFileHandler> registry = new ConcurrentHashMap<>();

    public void initHandlerMethods() {
        registerStaticFilesInDirectory(new File(STATIC_PATH));
    }

    private void registerStaticFilesInDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isFile()) {
                RequestMappingInfo mappingInfo = createRequestMappingInfo(file);
                StaticFileHandler handler = createStaticFileHandler(file);
                registry.put(mappingInfo, handler);
            }
            if (file.isDirectory()) {
                registerStaticFilesInDirectory(file);
            }
        }
    }

    private RequestMappingInfo createRequestMappingInfo(File file) {
        String relativePath = file.getPath().replaceFirst(STATIC_PATH, "");
        return new RequestMappingInfo(relativePath, HttpMethod.GET);
    }

    private StaticFileHandler createStaticFileHandler(File file) {
        return new StaticFileHandler(file);
    }

    @Override
    public StaticFileHandler getHandler(HttpRequest request) {
        RequestMappingInfo lookupRequestMapping = new RequestMappingInfo(request.getUri().getPath(), request.getMethod());
        return registry.get(lookupRequestMapping);
    }
}
