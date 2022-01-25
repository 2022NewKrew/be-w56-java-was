package webserver.http;

import common.CharsetUtil;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static common.ObjectUtil.checkNonEmptyAfterTrim;
import static common.ObjectUtil.checkPositiveOrZero;

public class HttpVersion {
    private static final Pattern VERSION_PATTERN = Pattern.compile("(\\S+)/(\\d+)\\.(\\d+)");

    private static final String HTTP_1_0_STRING = "HTTP/1.0";
    private static final String HTTP_1_1_STRING = "HTTP/1.1";

    public static final HttpVersion HTTP_1_0 = new HttpVersion("HTTP", 1, 0, false, true);
    public static final HttpVersion HTTP_1_1 = new HttpVersion("HTTP", 1, 1, true, true);

    private final String protocalName;
    private final int majorVersion;
    private final int minorVersion;
    private final String text;
    private final boolean keepAliveDefault;
    private final byte[] bytes;

    public HttpVersion(String text, boolean keepAliveDefault) {
        text = checkNonEmptyAfterTrim(text, "text").toUpperCase();

        Matcher m = VERSION_PATTERN.matcher(text);
        if(!m.matches()) {
            throw new IllegalArgumentException("invalid version format : " + text);
        }

        protocalName = m.group(1);
        majorVersion = Integer.parseInt(m.group(2));
        minorVersion = Integer.parseInt(m.group(3));
        this.text = protocalName + '/' + majorVersion + '.' + minorVersion;
        this.keepAliveDefault = keepAliveDefault;
        bytes = null;
    }

    public HttpVersion(String protocalName, int majorVersion, int minorVersion, boolean keepAliveDefault) {
        this(protocalName, majorVersion, minorVersion, keepAliveDefault, false);
    }

    public HttpVersion(String protocalName, int majorVersion, int minorVersion, boolean keepAliveDefault, boolean bytes) {
        protocalName = checkNonEmptyAfterTrim(protocalName, "protocolName").toUpperCase();

        for (int i = 0; i < protocalName.length(); i++) {
            if (Character.isISOControl(protocalName.charAt(i)) ||
                    Character.isWhitespace(protocalName.charAt(i))) {
                throw new IllegalArgumentException("invalid character in protocolName");
            }
        }

        checkPositiveOrZero(majorVersion, "majorVersion");
        checkPositiveOrZero(minorVersion, "minorVersion");

        this.protocalName = protocalName;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        text = protocalName + '/' + majorVersion + '.' + minorVersion;
        this.keepAliveDefault = keepAliveDefault;

        if(bytes) {
            this.bytes = text.getBytes(StandardCharsets.US_ASCII);
        } else {
            this.bytes = null;
        }
    }

    public String getProtocalName() {
        return protocalName;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public String getText() {
        return text;
    }

    public boolean isKeepAliveDefault() {
        return keepAliveDefault;
    }

    @Override
    public String toString() {
        return getText();
    }

    void encode(ByteBuf buf) {
        if(bytes == null) {
            buf.writeCharSequence(text, CharsetUtil.US_ASCII);
        } else {
            buf.writeBytes(bytes);
        }
    }
}
