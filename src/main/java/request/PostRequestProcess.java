package request;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import util.IOUtils;
import util.ResponseData;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class PostRequestProcess implements RequestProcess{
    private String url;
    private String queryString;
    private ResponseData responseData;
    public PostRequestProcess(String url, BufferedReader br) throws IOException {
        this.url = url;
        int contentLength = 0;
        while(true) {
            String line = br.readLine();
            String[] tokens = line.split(": ");
            if (tokens[0].equals("Content-Length"))
                contentLength = Integer.valueOf(tokens[1]);
            RequestHandler.log.debug("POST READ : " + line);
            if (line.equals("") || line == null) break;
        };
        queryString = IOUtils.readData(br, contentLength);
        RequestHandler.log.debug("query String " + queryString);
    }

    @Override
    public void process() {
        if("/user/create".equals(url)) {
            RequestHandler.log.debug("process : /user/create");
            Map<String, String> parsedQuery = HttpRequestUtils.parseQueryString(queryString);
            User user = new User(parsedQuery.get("userId"),
                    parsedQuery.get("password"),
                    parsedQuery.get("name"),
                    parsedQuery.get("email"));
            RequestHandler.log.debug(user.toString());
            DataBase.addUser(user);
            responseData = new ResponseData(getRedirectResponseHeader("/"), " ".getBytes());
        }
    }

    @Override
    public ResponseData getResponseData() {
        return responseData;
    }

    private String getRedirectResponseHeader(String url) {
        return "HTTP/1.1 302 FOUND\r\n"+
                "Location: "+url+"\r\n"+
                "\r\n";
    }
}
