package webserver.http.connection;

public class SocketChannelIO implements ConnectionIO {

    @Override
    public String readLine() {
        return null;
    }

    @Override
    public int read(char[] cbuf, int off, int len) {
        return 0;
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
