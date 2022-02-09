package http.view;

import com.google.common.base.Strings;

import java.io.BufferedReader;
import java.io.IOException;

public class InputView {
    public static String[] readTokenizedLine(BufferedReader br, String regex) throws IOException {
        String line = br.readLine();
        if (Strings.isNullOrEmpty(line)) {
            return new String[0];
        }

        return line.split(regex);
    }
}
