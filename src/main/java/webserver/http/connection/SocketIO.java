package webserver.http.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketIO implements ConnectionIO {
    private static final Logger log = LoggerFactory.getLogger(SocketIO.class);

    private InputStream in;
    private OutputStream out;

    public SocketIO(Socket socket) {
        try {
            this.in = socket.getInputStream();
            this.out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] readAllBytes() {
        try {
            return in.readAllBytes();
        } catch (IOException e) {
            log.error(e.getMessage());
            return new byte[0];
        }
    }

    @Override
    public void writeAllBytes(byte[] buffer) {
        try {
            out.write(buffer);
            out.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
