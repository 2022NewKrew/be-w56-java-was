package util;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class IOUtils {

    private static final String BLANK = "";
    private static final String SPACE = " ";

    public static String[] readRequestLine(BufferedReader br) throws IOException {
        String tokens = br.readLine();
        return tokens.split(SPACE);
    }

    public static List<String> readHeaders(BufferedReader br) throws IOException {
        List<String> lines = Lists.newArrayList();
        String line;
        while (!(line = br.readLine()).equals(BLANK)) {
            lines.add(line);
        }
        return lines;
    }

    public static String readBody(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }
}
