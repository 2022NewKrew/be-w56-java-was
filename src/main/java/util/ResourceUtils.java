package util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ResourceUtils {

    public static byte[] getResource(String resourceName) {
        byte[] resource;
        try {
            URL url = getUrl(resourceName);
            resource = url.openStream().readAllBytes();
        } catch (IOException | NullPointerException e) {
            throw new IllegalArgumentException("invalid resource name: " + resourceName);
        }
        return resource;
    }

    public static boolean existResource(String resourceName) {
        File file = null;
        try {
            URL url = getUrl(resourceName);
            file = new File(url.toURI());
        } catch (Exception e) {

        }
        return file != null && file.isFile();
    }

    private static URL getUrl(String resourceName) {
        if (resourceName.charAt(0) != '/') {
            resourceName = "/" + resourceName;
        }

        return ResourceUtils.class.getResource(resourceName);
    }
}
