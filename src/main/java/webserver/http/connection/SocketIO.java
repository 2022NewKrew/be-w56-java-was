package webserver.http.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketIO implements ConnectionIO {
    private static final Logger log = LoggerFactory.getLogger(SocketIO.class);

    private BufferedReader br;
    private DataOutputStream dos;

    public SocketIO(Socket socket) {
        try {
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            this.dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String readLine() {
        try {
            return br.readLine();
        } catch (IOException e) {
            log.error(e.getMessage());
            return "";
        }
    }

    @Override
    public int read(char[] cbuf, int off, int len) {
        try {
            return br.read(cbuf, off, len);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void writeBytes(String s) {
        try {
            dos.writeBytes(s);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void write(byte[] b, int off, int len) {
        try {
            dos.write(b, off, len);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void flush() {
        try {
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
