package util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;

public class ReadUrl {

    private static final Logger log = LoggerFactory.getLogger(ReadUrl.class);

    public static String parseUrl(String line, String method){

        String[] tokens = line.split(" ");
        if (tokens[0].equals("GET")) {
            return tokens[1];
        }
        return "";
    }

    public static byte[] openUrl(String url){

        try{
            byte[] openFile = Files.readAllBytes(new File("./webapp" + url).toPath());
            return openFile;
        } catch(Exception e){
            byte[] openFile = {};
            return openFile;
        }
    }

}
