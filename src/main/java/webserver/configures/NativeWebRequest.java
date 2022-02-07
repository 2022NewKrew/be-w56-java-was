package webserver.configures;

public interface NativeWebRequest {
    String getParameter(String offsetParameterName);

    Object getNativeRequest();
}
