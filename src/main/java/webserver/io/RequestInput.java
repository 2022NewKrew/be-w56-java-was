package webserver.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RequestInput {
    BufferedReader br;

    public RequestInput(InputStream is) {
        this.br = new BufferedReader(new InputStreamReader(is));
    }

    public String readLine() {
        String result = null;
        try {
            result = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String read(int contentLength) {
        char[] result = new char[contentLength];
        try {
            br.read(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return String.valueOf(result);
    }
}
