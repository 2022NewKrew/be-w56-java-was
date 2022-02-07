package util.response;

import java.util.Set;

public enum FileType {
    STRING, BYTES, NONE;

    private static final Set<String> stringTypes = Set.of("html", "css", "js");
    private static final Set<String> byteTypes = Set.of("ico", "eot", "svg", "ttf", "woff", "woff2", "png");

    public static FileType getFileType(String fileName){
        String[] split = fileName.split("\\.");
        String extensionName = split[split.length-1];

        if(stringTypes.contains(extensionName)){
            return STRING;
        }

        if(byteTypes.contains(extensionName)){
            return BYTES;
        }

        return NONE;
    }
}
