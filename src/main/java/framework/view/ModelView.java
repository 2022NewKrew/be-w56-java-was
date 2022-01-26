package framework.view;

import framework.util.ContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.HttpStatus;
import org.apache.http.impl.EnglishReasonPhraseCatalog;
import org.apache.tika.Tika;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import static framework.util.Constants.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelView {
    private static final Tika TIKA = new Tika();

    private final Map<String, Object> attributes = new HashMap<>();
    private String url;
    private boolean isStatic;
    private int statusCode;
    private ContentType contentType;
    private int contentLength;
    private byte[] content;

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public String getStatusCodeString() {
        return EnglishReasonPhraseCatalog.INSTANCE.getReason(statusCode, null);
    }

    public void readStaticFile() throws Exception {
        String path = url;
        statusCode = HttpStatus.SC_OK;

        if (url.startsWith(REDIRECT_MARK)) {
            path = url.split(REDIRECT_MARK)[1] + DEFAULT_REDIRECT_EXTENSION;
            statusCode = HttpStatus.SC_MOVED_TEMPORARILY;
            url = path;
        }

        File file = new File(CONTEXT_PATH + path);
        contentType = ContentType.builder()
                .mime(TIKA.detect(file))
                .build();

        FileInputStream fis = new FileInputStream(file);
        contentLength = (int) file.length();
        content = new byte[contentLength];
        fis.read(content);
        fis.close();
    }
}
