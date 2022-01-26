package webserver.http;

import service.UserService;
import webserver.http.request.Request;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum HttpGetMapper{
    MAIN("/"){
        @Override
        public ResponseData service(Request request){
            return new ResponseData(HttpStatusCode.SUCCESS, "/index.html");
        }
    },
    SIGN_UP("/user/create"){
        @Override
        public ResponseData service(Request request){
            UserService.createUser(request.getQueries());
            return new ResponseData(HttpStatusCode.REDIRECT, "/");
        }
    },
    SIGN_UP_FORM("/user/form"){
        @Override
        public ResponseData service(Request request){
            return new ResponseData(HttpStatusCode.SUCCESS, "/user/form.html");
        }
    };

    private static final Map<String, HttpGetMapper> URI_CACHE =
            Stream.of(values()).collect(Collectors.toMap(HttpGetMapper::getUrl, Function.identity()));

    private final String url;

    HttpGetMapper(String url){
        this.url = url;
    }

    public static Optional<HttpGetMapper> valueOfUrl(String url){
        return Optional.ofNullable(URI_CACHE.get(url));
    }

    public String getUrl(){ return url; }

    public abstract ResponseData service(Request request);
}
