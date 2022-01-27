package http.resource;

public class Empty implements Resource{
    @Override
    public String getType() {
        return null;
    }

    @Override
    public byte[] getContent() {
        return new byte[0];
    }
}
