package model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Request {
    private String httpMethod;
    private String urlPath;
    private String respContextType;
    private Map<String, String> queryString;
}
