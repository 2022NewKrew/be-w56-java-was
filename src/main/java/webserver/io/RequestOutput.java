package webserver.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class RequestOutput {
    BufferedWriter bw;

    public RequestOutput() {
        this.bw = new BufferedWriter(new OutputStreamWriter(System.out));
    }

    public void write (String str) {
        try {
            bw.write("=== REQ :: ");
            bw.write(str);
            bw.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void flush () {
        try {
            bw.write("==========================================\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
