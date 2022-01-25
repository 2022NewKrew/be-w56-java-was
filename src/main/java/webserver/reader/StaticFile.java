package webserver.reader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StaticFile {

    private String fileFullName;

    private String fileName;

    private String extension;

    private byte[] contents;

    public StaticFile(String fileFullName, String fileName, String extension, byte[] contents) {
        this.fileFullName = fileFullName;
        this.fileName = fileName;
        this.extension = extension;
        this.contents = contents;
    }

    public static StaticFile create(String rootPath, String url) throws IOException {
        String fileName = url.equals("/") ? "/index.html" : url;
        String fileFullName = rootPath + fileName;
        byte[] contents = Files.readAllBytes(new File(fileFullName).toPath());

        String extension = "";
        if(fileFullName.lastIndexOf('.') != -1) {
            extension = fileFullName.substring(fileName.lastIndexOf('.'));
        }
        return new StaticFile(fileFullName, fileName, extension, contents);
    }

    public String getExtension() {
        return extension;
    }

    public Integer getContentsLength() {
        return contents.length;
    }

    public byte[] getContents() {
        return contents;
    }
}
