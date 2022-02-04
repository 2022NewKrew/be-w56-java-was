package framework.view;

import framework.util.ContentType;
import framework.util.HttpSession;
import framework.util.Invalidator;
import framework.util.ModelViewAttributes;
import framework.util.exception.StaticFileNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.HttpStatus;
import org.apache.http.impl.EnglishReasonPhraseCatalog;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import static framework.util.Constants.CONTEXT_PATH;
import static framework.util.Constants.REDIRECT_MARK;

/**
 * ModelView, Client에게 응답할 때 필요한 정보들을 담음
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelView {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelView.class);

    /**
     * Apache Tika, Static file의 MIME을 알아낼 때 활용
     */
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

    public ModelViewAttributes getAttributes() {
        return modelViewAttributes;
    }

    public String getStatusCodeString() {
        return EnglishReasonPhraseCatalog.INSTANCE.getReason(statusCode, null);
    }

    public void addSessionAttributes(HttpSession session) {
        for (Map.Entry<String, Object> entry : session.entrySet()) {
            modelViewAttributes.setAttribute(entry.getKey(), entry.getValue());
        }
    }

    public void readStaticFile() {
        statusCode = HttpStatus.SC_OK;

        // HTML 파일일 경우, 동적으로 HTML을 만들어서 저장
        if (uri.endsWith(".html")) {
            contentType = ContentType.builder()
                    .mime("text/html").build();

            content = MustacheResolver.readHtmlFile(CONTEXT_PATH + uri, modelViewAttributes);
            contentLength = content.length;
            return;
        }

        if (Invalidator.isStaticFile(CONTEXT_PATH + "/" + uri + ".html")) {
            contentType = ContentType.builder()
                    .mime("text/html").build();

            content = MustacheResolver.readHtmlFile(CONTEXT_PATH + "/" + uri + ".html", modelViewAttributes);
            contentLength = content.length;
            return;
        }

        // 그 외 파일일 경우
        readOtherFile(CONTEXT_PATH + uri);
    }

    private void readOtherFile(String absolutePath) {
        try {
            File file = new File(absolutePath);
            contentType = ContentType.builder()
                    .mime(TIKA.detect(file))
                    .build();

            FileInputStream fis = new FileInputStream(file);
            contentLength = (int) file.length();
            content = new byte[contentLength];
            int returned = fis.read(content);
            fis.close();

            if (returned == -1) {
                throw new StaticFileNotFoundException();
            }
        } catch (IOException e) {
            throw new StaticFileNotFoundException();
        }
    }

    public void redirect() {
        statusCode = HttpStatus.SC_MOVED_TEMPORARILY;

        uri = uri.split(REDIRECT_MARK)[1];

        // Welcome Page 설정
        if ("/".equals(uri) || "/index".equals(uri)) {
            uri = "/index.html";
        }
    }

    public void readAttributes() {
        String attributesStr = modelViewAttributes.parseAttributesToJson();
    }

    public void copy(ModelView forCopy) {
        this.modelViewAttributes.copy(forCopy.getAttributes());
        this.uri = forCopy.getUri();
        this.isStatic = forCopy.isStatic();
        this.statusCode = forCopy.getStatusCode();
        this.contentType = forCopy.getContentType();
        this.contentLength = forCopy.getContentLength();
        this.content = forCopy.getContent();
    }
}
