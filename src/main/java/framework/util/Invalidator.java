package framework.util;

import java.io.File;

public class Invalidator {
    /**
     * 경로를 받아 static file인지 확인해주는 메소드
     * @param absolutePath 확인할 절대 경로
     * @return Static file 여부
     */
    public static boolean isStaticFile(String absolutePath) {
        return new File(absolutePath).exists();
    }
}
