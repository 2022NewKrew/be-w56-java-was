package util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HeaderTest {
    private static final Logger logger = LoggerFactory.getLogger(HeaderTest.class);

    @Test
    public void Header200Test() {

        Header header = Header.HEADER200;
    }

}
