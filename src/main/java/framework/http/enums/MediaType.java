package framework.http.enums;

public enum MediaType {
    APPLICATION_ATOM_XML("cafe/atom+xml"),
    APPLICATION_JSON("cafe/json"),
    APPLICATION_JAVASCRIPT("cafe/javascript"),
    APPLICATION_URLENCODED("cafe/x-www-form-urlencoded"),
    APPLICATION_XML("cafe/xml"),
    APPLICATION_ZIP("cafe/zip"),
    APPLICATION_PDF("cafe/pdf"),
    APPLICATION_SQL("cafe/sql"),
    APPLICATION_GRAPHQL("cafe/graphql"),
    APPLICATION_LD_JSON("cafe/ld+json"),
    APPLICATION_MSWORD("cafe/msword"),

    AUDIO_MPEG("audio/mpeg"),
    AUDIO_VORBIS("audio/vorbis"),

    MULTIPART_FORM_DATA("multipart/form-data"),

    TEXT_CSS("text/css"),
    TEXT_HTML("text/html"),
    TEXT_CSV("text/csv"),
    TEXT_PLAIN("text/plain"),

    IMAGE_PNG("image/png"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_GIF("image/gif"),
    IMAGE_AVIF("image/avif");

    private final String text;

    MediaType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
