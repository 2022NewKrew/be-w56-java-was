package mapper;

import Controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class UrlMapper {
    private final Map<String, Controller> controllerMap;
    private static final Logger log = LoggerFactory.getLogger(UrlMapper.class);

    public UrlMapper(){
        controllerMap = new HashMap<String, Controller>();


    }

    public void transferSocketToController(Socket socket){

    }
}
