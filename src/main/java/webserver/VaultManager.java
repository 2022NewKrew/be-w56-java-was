package webserver;

import util.HttpConnectionUtils;
import util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VaultManager {
    private static final String VAULT_URL = "https://vault-beta.daumkakao.io/v1";
    private Map<String, String> secrets;

    public void initialize() {
        Properties properties = (Properties) SingletonBeanRegistry.getBean("Properties");
        String roleId = properties.getProperty("role-id");
        String secretId = properties.getProperty("secret-id");
        String token = getTokenFromApi(roleId, secretId);
        String secretUrl = properties.getProperty("secret-url");
        secrets = getSecretsFromApi(secretUrl, token);
    }

    private String getTokenFromApi(String roleId, String secretId) {
        String loginFormat = "{\"role_id\": \"%s\", \"secret_id\": \"%s\"}";
        try {
            String tokenResponse = HttpConnectionUtils.sendPostWithBody(
                    VAULT_URL + "/auth/approle/login",
                    String.format(loginFormat, roleId, secretId)
            );
            Pattern pattern = Pattern.compile("\"client_token\":\"([a-zA-Z0-9.]*)\"");
            Matcher matcher = pattern.matcher(tokenResponse);
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, String> getSecretsFromApi(String secretUrl, String token) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Vault-Token", token);
        String secretResponse;
        try {
            secretResponse = HttpConnectionUtils.sendGetWithHeader(
                    VAULT_URL + secretUrl, headers
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Pattern pattern = Pattern.compile("\"data\":\\{(.*)},");
        Matcher matcher = pattern.matcher(secretResponse);
        if (!matcher.find()) {
            throw new RuntimeException("Invalid response of vault secret");
        }
        return StringUtils.parseJsonString(matcher.group(1));
    }

    public Map<String, String> getSecrets() {
        return secrets;
    }
}
