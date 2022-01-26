package http;

public enum MediaType {
    APPLICATION_ATOM_XML("application/atom+xml"),
    APPLICATION_JSON("application/json"),
    APPLICATION_JAVASCRIPT("application/javascript"),
    APPLICATION_URLENCODED("application/x-www-form-urlencoded"),
    APPLICATION_XML("application/xml"),
    APPLICATION_ZIP("application/zip"),
    APPLICATION_PDF("application/pdf"),
    APPLICATION_SQL("application/sql"),
    APPLICATION_GRAPHQL("application/graphql"),
    APPLICATION_LD_JSON("application/ld+json"),
    APPLICATION_MSWORD("application/msword"),

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
