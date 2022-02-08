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
import org.apache.commons.codec.binary.StringUtils;
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

    /** ModelView 내에서 사용할 Attribute들을 저장할 객체 */
    private final ModelViewAttributes modelViewAttributes = new ModelViewAttributes();

    private String uri;
    private boolean isStatic;
    private int statusCode;
    private ContentType contentType;
    private int contentLength;
    private byte[] content;
    private boolean isResponseBody;

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

    /**
     * Session 내용들을 현재 ModelView Attribute에 추가해주는 메소드
     * @param session HTTP Session
     */
    public void addSessionAttributes(HttpSession session) {
        for (Map.Entry<String, Object> entry : session.entrySet()) {
            modelViewAttributes.setAttribute(entry.getKey(), entry.getValue());
        }
    }

    /**
     * ResponseBody로 설정돼있는 경우, Response의 Body에 JSON 형식으로 Attribute들을 넣어주는 메소드
     */
    public void readAttributesToJson() {
        if (!isResponseBody) {
            return;
        }

        String parsedStr = parseAttributesToJson();

        statusCode = HttpStatus.SC_OK;
        content = StringUtils.getBytesUtf8(parsedStr);
        contentLength = content.length;
        isStatic = false;
    }

    /**
     * 현재 Attributes 정보들을 JSON 형태의 String으로 파싱해주는 메소드
     */
    public String parseAttributesToJson() {
        return modelViewAttributes.parseAttributesToJson();
    }

    /**
     * Static file을 읽어 ModelView의 내용을 채워주는 메소드
     */
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

        // HTML 확장자를 붙였을 때 존재할 경우, 동적으로 HTML을 만들어서 저장
        if (Invalidator.isStaticFile(CONTEXT_PATH + "/" + uri + ".html")) {
            contentType = ContentType.builder()
                    .mime("text/html").build();

            content = MustacheResolver.readHtmlFile(CONTEXT_PATH + "/" + uri + ".html", modelViewAttributes);
            contentLength = content.length;
            return;
        }

        // 그 외 파일일 경우 해당 파일 내용을 가져와서 저장
        readOtherFile(CONTEXT_PATH + uri);
    }

    /**
     * HTML 외의 파일을 불러와서 해당 내용을 byte 배열에 저장해주는 메소드
     * @param absolutePath 파일의 절대 경로
     */
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

    /**
     * Redirect를 하도록 설정해주는 메소드
     */
    public void redirect() {
        // 상태 코드를 302로 설정하여 클라이언트가 다시 요청하도록 함
        statusCode = HttpStatus.SC_MOVED_TEMPORARILY;

        uri = uri.split(REDIRECT_MARK)[1];

        // Welcome Page 설정
        if ("/".equals(uri) || "/index".equals(uri)) {
            uri = "/index.html";
        }
    }

    /**
     * 받은 ModelView의 내용을 현재 ModelView에 모두 복사해주는 메소드
     * @param forCopy 복사할 ModelView 객체
     */
    public void copy(ModelView forCopy) {
        this.modelViewAttributes.copy(forCopy.getAttributes());
        this.uri = forCopy.getUri();
        this.isStatic = forCopy.isStatic();
        this.statusCode = forCopy.getStatusCode();
        this.contentType = forCopy.getContentType();
        this.contentLength = forCopy.getContentLength();
        this.content = forCopy.getContent();
        this.isResponseBody = forCopy.isResponseBody();
    }
}
