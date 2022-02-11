package webserver.http.connection;

public interface ConnectionIO {
    String readLine();
    void writeBytes(String s);
    void write(byte[] b, int off, int len);
    void flush();
}
