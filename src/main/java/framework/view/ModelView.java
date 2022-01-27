package framework.view;

import framework.util.ContentType;
import framework.util.ModelViewAttributes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.HttpStatus;
import org.apache.http.impl.EnglishReasonPhraseCatalog;
import org.apache.tika.Tika;

import java.io.File;
import java.io.FileInputStream;

import static framework.util.Constants.*;

/**
 * ModelView, Client에게 응답할 때 필요한 정보들을 담음
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelView {
    /** Apache Tika, Static file의 MIME을 알아낼 때 활용 */
    private static final Tika TIKA = new Tika();

    private final ModelViewAttributes modelViewAttributes = new ModelViewAttributes();
    private String uri;
    private boolean isStatic;
    private int statusCode;
    private ContentType contentType;
    private int contentLength;
    private byte[] content;

    public void setAttribute(String key, Object value) {
        modelViewAttributes.setAttribute(key, value);
    }

    public Object getAttribute(String key) {
        return modelViewAttributes.getAttribute(key);
    }

    public String getStatusCodeString() {
        return EnglishReasonPhraseCatalog.INSTANCE.getReason(statusCode, null);
    }

    public void readStaticFile() throws Exception {
        String filePath = uri;
        statusCode = HttpStatus.SC_OK;

        if (uri.startsWith(REDIRECT_MARK)) {
            filePath = uri.split(REDIRECT_MARK)[1] + DEFAULT_REDIRECT_EXTENSION;
            statusCode = HttpStatus.SC_MOVED_TEMPORARILY;
            uri = filePath;
        }

        File file = new File(CONTEXT_PATH + filePath);
        contentType = ContentType.builder()
                .mime(TIKA.detect(file))
                .build();

        FileInputStream fis = new FileInputStream(file);
        contentLength = (int) file.length();
        content = new byte[contentLength];
        fis.read(content);
        fis.close();
    }

    public void readAttributes() {
        String attributesStr = modelViewAttributes.parseAttributesToJson();
    }
}
