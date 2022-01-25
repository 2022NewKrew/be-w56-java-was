package webserver.manage;

import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ResponseFile {
    File file;

    private static final String ROOT_DIRECTORY = "./webapp";
    public static final String ERROR_FILE = "/index.html";

    public ResponseFile(String filePath) throws RuntimeException {
        File newFile = new File(ROOT_DIRECTORY + filePath);
        if(!checkFileIsExist(newFile)) {
            throw new RuntimeException();
        }
        this.file = newFile;
    }

    private boolean checkFileIsExist(File file) {
        return file.exists() && file.isFile();
    }

    public byte[] getFileBytes () {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFileType () {
        try {
            return new Tika().detect(file);
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

}
