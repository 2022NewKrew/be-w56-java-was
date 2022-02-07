package util;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class IOUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(IOUtilsTest.class);

    @Test
    public void readData() throws Exception {
        String data = "abcd123";
        StringReader sr = new StringReader(data);
        BufferedReader br = new BufferedReader(sr);

        logger.debug("parse body : {}", IOUtils.readData(br, data.length()));
    }

    @DisplayName("MimeType 출력 여부 확인")
    @Test
    public void readMimeType() throws Exception {
        String icoUrl = "./webapp/favicon.ico";
        String jsUrl = "./webapp/js/scripts.js";
        String cssUrl = "./webapp/css/styles.css";
        String htmlUrl = "./webapp/index.html";

        assertThat(IOUtils.readMimeType(icoUrl)).isEqualTo("image/vnd.microsoft.icon");
        assertThat(IOUtils.readMimeType(jsUrl)).isEqualTo("application/javascript");
        assertThat(IOUtils.readMimeType(cssUrl)).isEqualTo("text/css");
        assertThat(IOUtils.readMimeType(htmlUrl)).isEqualTo("text/html");
    }

    @DisplayName("텝플릿엔진용 테스트")
    @Test
    public void templateEngine() throws Exception {
        String a = "<h1>{{user}} {{name}}</h1>";
//        String pattern = "^\\{\\{ \\S+ \\}\\}$";
        String pattern = "\\{\\{[a-zA-Z]+\\}\\}";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(a);

        while(matcher.find()){
            String found = matcher.group();
            System.out.println(found);
            a = a.replaceFirst(pattern, example(found));
            System.out.println(a);
        }


        assertThat(a).isEqualTo("<h1>foundUser mylo</h1>");
    }

    String example(String tc){
        Map<String, String> testMap = new HashMap<>();
        testMap.put("user", "foundUser");
        testMap.put("name", "mylo");

        tc = tc.substring(2, tc.length()-2);
        return testMap.get(tc);
    }
}
