package util;

import java.io.BufferedReader;
import java.io.StringReader;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.annotation.RequestMapping;

public class IOUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(IOUtilsTest.class);

    @Test
    public void readData() throws Exception {
        String data = "abcd123";
        StringReader sr = new StringReader(data);
        BufferedReader br = new BufferedReader(sr);

        logger.debug("parse body : {}", IOUtils.readData(br, data.length()));
    }

    @Test
    public void temp(){
        //System.out.println(HandlerMapper.findAllClasses("webserver.controller"));
        ReflectionUtils.findAllMethods("webserver.controller").forEach(item -> {
            System.out.println(item);
            System.out.println(item.getDeclaredAnnotation(RequestMapping.class));
            if(item.getDeclaredAnnotation(RequestMapping.class)!=null) System.out.println(item.getDeclaredAnnotation(RequestMapping.class).value());
        });
    }
}
