package webserver.http.connection;

public class SocketChannelIO implements ConnectionIO {

    @Override
    public String readLine() {
        return null;
    }

    @Override
    public void writeBytes(String s) {

    }

    @Override
    public void write(byte[] b, int off, int len) {

    }

    @Override
    public void flush() {

    }
}
