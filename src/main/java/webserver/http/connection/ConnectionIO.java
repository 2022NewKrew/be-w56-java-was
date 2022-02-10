package webserver.http.connection;

public interface ConnectionIO {
    byte[] readAllBytes();
    void writeAllBytes(byte[] buffer);
}
