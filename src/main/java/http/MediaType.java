package http;

import exception.ContentTypeNotFoundException;
import java.util.Arrays;

public enum MediaType {
    ALL("", "*/*"),
    AUDIO_AAC("aac", "audio/aac"),
    APPLICATION_X_ABIWORD("abw", "application/x-abiword"),
    APPLICATION_X_FREEARC("arc", "application/x-freearc"),
    VIDEO_X_MSVIDEO("avi", "video/x-msvideo"),
    APPLICATION_VND_AMAZON_EBOOK("azw", "application/vnd.amazon.ebook"),
    APPLICATION_OCTET_STREAM("bin", "application/octet-stream"),
    IMAGE_BMP("bmp", "image/bmp"),
    APPLICATION_X_BZIP("bz", "application/x-bzip"),
    APPLICATION_X_BZIP2("bz2", "application/x-bzip2"),
    APPLICATION_X_CSH("csh", "application/x-csh"),
    TEXT_CSS("css", "text/css"),
    TEXT_CSV("csv", "text/csv"),
    APPLICATION_MSWORD("doc", "application/msword"),
    APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_WORDPROCESSINGML_DOCUMENT("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    APPLICATION_VND_MS_FONTOBJECT("eot", "application/vnd.ms-fontobject"),
    APPLICATION_EPUB_ZIP("epub", "application/epub+zip"),
    APPLICATION_GZIP("gz", "application/gzip"),
    IMAGE_GIF("gif", "image/gif"),
    TEXT_HTML("html", "text/html"),
    IMAGE_VND_MICROSOFT_ICON("ico", "image/vnd.microsoft.icon"),
    ITEXT_CALENDAR("ics", "itext/calendar"),
    APPLICATION_JAVA_ARCHIVE("jar", "application/java-archive"),
    IMAGE_JPEG("jpeg", "image/jpeg"),
    APPLICATION_JAVASCRIPT("js", "application/javascript"),
    APPLICATION_JSON("json", "application/json"),
    APPLICATION_LD_JSON("jsonld", "application/ld+json"),
    AUDIO_MIDI("midi", "audio/midi"),
    TEXT_JAVASCRIPT("mjs", "text/javascript"),
    AUDIO_MPEG("mp3", "audio/mpeg"),
    VIDEO_MPEG("mpeg", "video/mpeg"),
    APPLICATION_VND_APPLE_INSTALLER_XML("mpkg", "application/vnd.apple.installer+xml"),
    APPLICATION_VND_OASIS_OPENDOCUMENT_PRESENTATION("odp", "application/vnd.oasis.opendocument.presentation"),
    APPLICATION_VND_OASIS_OPENDOCUMENT_SPREADSHEET("ods", "application/vnd.oasis.opendocument.spreadsheet"),
    APPLICATION_VND_OASIS_OPENDOCUMENT_TEXT("odt", "application/vnd.oasis.opendocument.text"),
    AUDIO_OGG("oga", "audio/ogg"),
    VIDEO_OGG("ogv", "video/ogg"),
    APPLICATION_OGG("ogx", "application/ogg"),
    AUDIO_OPUS("opus", "audio/opus"),
    FONT_OTF("otf", "font/otf"),
    IMAGE_PNG("png", "image/png"),
    APPLICATION_PDF("pdf", "application/pdf"),
    APPLICATION_X_HTTPD_PHP("php", "application/x-httpd-php"),
    APPLICATION_VND_MS_POWERPOINT("ppt", "application/vnd.ms-powerpoint"),
    APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_PRESENTATIONML_PRESENTATION("pptx",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    APPLICATION_VND_RAR("rar", "application/vnd.rar"),
    APPLICATION_RTF("rtf", "application/rtf"),
    APPLICATION_X_SH("sh", "application/x-sh"),
    IMAGE_SVG_XML("svg", "image/svg+xml"),
    APPLICATION_X_SHOCKWAVE_FLASH("swf", "application/x-shockwave-flash"),
    APPLICATION_X_TAR("tar", "application/x-tar"),
    IMAGE_TIFF("tiff", "image/tiff"),
    FONT_TTF("ttf", "font/ttf"),
    TEXT_PLAIN("txt", "text/plain"),
    APPLICATION_VND_VISIO("vsd", "application/vnd.visio"),
    AUDIO_WAV("wav", "audio/wav"),
    AUDIO_WEBM("weba", "audio/webm"),
    VIDEO_WEBM("webm", "video/webm"),
    IMAGE_WEBP("webp", "image/webp"),
    FONT_WOFF("woff", "font/woff"),
    FONT_WOFF2("woff2", "font/woff2"),
    APPLICATION_XHTML_XML("xhtml", "application/xhtml+xml"),
    APPLICATION_VND_MS_EXCEL("xls", "application/vnd.ms-excel"),
    APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_SPREADSHEETML_SHEET("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    APPLICATION_XML("xml", "application/xml"),
    APPLICATION_X_WWW_FORM_URLENCODED("", "application/x-www-form-urlencoded"),
    ;

    private static final String EXTENSION_DELIMITER = ".";

    private final String extension;
    private final String type;

    MediaType(String extension, String type) {
        this.extension = extension;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static MediaType matchType(String type) {
        return Arrays.stream(MediaType.values())
                .filter(value -> type.equals(value.type))
                .findAny()
                .orElseThrow(ContentTypeNotFoundException::new);
    }

    public static MediaType getMediaType(String path) {
        String extension = getFileExtension(path);

        return Arrays.stream(MediaType.values())
                .filter(value -> extension.equals(value.extension))
                .findAny()
                .orElse(ALL);
    }

    private static String getFileExtension(String path) {
        int extensionIdx = path.lastIndexOf(EXTENSION_DELIMITER);

        return path.substring(extensionIdx + 1);
    }
}
