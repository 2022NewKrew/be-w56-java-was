package service;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

public class WebService {

    private static final String URL_PREFIX = "./webapp";
    private static final Logger log = LoggerFactory.getLogger(WebService.class);

    public static HashMap<String, String> parseRequest(BufferedReader bufferedReader) throws Exception {

        String method = "";
        String toURL = "";
        String contentLength = "";

        while(true){
            String next = bufferedReader.readLine().trim();
            log.debug(next);
            if(next.isEmpty()){
                break;
            }
            String[] splitLine = next.split(" ");
            if (splitLine[0].equals("POST")) {
                method = "POST";
                toURL = splitLine[1];
            }
            if (splitLine[0].equals("GET")) {
                method = "GET";
                toURL = splitLine[1];
            }
            if (splitLine[0].equals("Content-Length:")) {
                contentLength = splitLine[1];
            }
        }

        log.debug("Extract results, method : {}, toURL : {}, contentLength : {}", method, toURL, contentLength);
        HashMap<String, String> requestParse = new HashMap<>();
        requestParse.put("method", method);
        requestParse.put("URL", toURL);
        requestParse.put("contentLength", contentLength);

        if (method.equals("POST") & !contentLength.isEmpty()){
            String body = IOUtils.readData(bufferedReader, Integer.parseInt(contentLength));
            log.debug("print body {}", body);
            requestParse.put("body", body);
        }

        return requestParse;
    }

    public static User createUser(String testURL){

        String[] parameter = testURL.split("&");
        HashMap<String, String> parameterMap = new HashMap<>();

        Arrays.stream(parameter).forEach(s -> parameterMap.put(s.split("=")[0], s.split("=")[1]));
        log.debug("createUser, parameter get {}", parameterMap.toString());

        User createUser = new User(parameterMap.get("userId"), parameterMap.get("password"), parameterMap.get("name"), parameterMap.get("email"));
        DataBase.addUser(createUser);

        return createUser;

    }
    public static String extractFunction(String ansURL){
        String[] list = ansURL.split("\\?")[0].split("/");
        return list[list.length-1];
    }

    public static byte[] openUrl(String url){

        String fullURL = URL_PREFIX + url;
        Path openURL = Paths.get(fullURL);

        try{
            byte[] openFile = Files.readAllBytes(openURL);
            return openFile;
        } catch(Exception e){
            log.debug("openURL exception {}", openURL);
            byte[] openFile = {};
            return openFile;
        }
    }

}
