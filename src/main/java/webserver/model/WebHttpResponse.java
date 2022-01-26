package webserver.model;

import java.io.DataOutputStream;

public class WebHttpResponse {
    private final DataOutputStream dos;
    private String result;

    private WebHttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public static WebHttpResponse of(DataOutputStream dos) {
        return new WebHttpResponse(dos);
    }

    public String getResult() {
        return result;
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
