package webserver.http.connection;

public interface ConnectionIO {
    String readLine();
    int read(char[] cbuf, int off, int len);
    void writeBytes(String s);
    void write(byte[] b, int off, int len);
    void flush();
}
