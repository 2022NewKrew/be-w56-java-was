package service;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class WebService {

    public static final String URL_PREFIX = "./webapp";
    private static final Logger log = LoggerFactory.getLogger(WebService.class);

    public static HashMap<String, String> parseRequest(BufferedReader bufferedReader) throws Exception {

        String method = "";
        String toURL = "";
        String type = "html";
        String contentLength = "";

        while(true){
            String next = bufferedReader.readLine().trim();
            log.debug("Next {}", next);
            if(next.isEmpty()){
                break;
            }
            String[] splitLine = next.split(" ");
            if (splitLine[0].equals("POST")) {
                method = "POST";
                toURL = splitLine[1];
                String[] subString = toURL.split("\\.");
                type = (subString.length > 0)? subString[subString.length-1] : "html";
            }
            if (splitLine[0].equals("GET")) {
                method = "GET";
                toURL = splitLine[1];
                String[] subString = toURL.split("\\.");
                type = (subString.length > 0)? subString[subString.length-1] : "html";
            }
            if (splitLine[0].equals("Content-Length:")) {
                contentLength = splitLine[1];
            }
        }

        log.debug("Extract results, method : {}, toURL : {}, contentLength : {}, type {}", method, toURL, contentLength, type);
        HashMap<String, String> requestParse = new HashMap<>();
        requestParse.put("method", method);
        requestParse.put("URL", toURL);
        requestParse.put("contentLength", contentLength);
        //TODO type 별 다른 응답
        type = (type.equals("css")) ? "css" : "html";
        requestParse.put("type", type);

        if (method.equals("POST") & !contentLength.isEmpty()){
            String body = IOUtils.readData(bufferedReader, Integer.parseInt(contentLength));
            requestParse.put("body", body);
        }

        return requestParse;
    }

    public static HashMap<String, String> parseURLBody(String body){
        String[] parameter = body.split("&");
        HashMap<String, String> parameterMap = new HashMap<>();

        Arrays.stream(parameter).forEach(s -> parameterMap.put(s.split("=")[0], s.split("=")[1]));
        log.debug("parseURLBody, parameter get {}", parameterMap.toString());

        return parameterMap;
    }

    public static String extractFunction(String ansURL){
        String[] list = ansURL.split("\\?")[0].split("/");

        if (list.length > 0){
            return list[list.length-1];
        }
        return "";
    }

    public static byte[] openUrl(String url){

        String fullURL = URL_PREFIX + url;
        Path openURL = Paths.get(fullURL);

        try{
            return Files.readAllBytes(openURL);
        } catch(Exception e){
            log.debug("openURL exception {}", openURL);
            return new byte[]{};
        }
    }

}
