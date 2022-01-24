package util;

import java.io.File;

public class Constants {
    public static final String PROJECT_PATH = new File(".").getAbsoluteFile().getPath();
    public static final String CONTEXT_PATH = PROJECT_PATH.substring(0, PROJECT_PATH.length() - 1) + "webapp";
}
