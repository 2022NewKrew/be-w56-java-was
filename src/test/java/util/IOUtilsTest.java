package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class IOUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(IOUtilsTest.class);

    @Test
    public void readData() throws Exception {
        String data = "abcd123";
        StringReader sr = new StringReader(data);
        BufferedReader br = new BufferedReader(sr);

        logger.debug("parse body : {}", IOUtils.readData(br, data.length()));
    }

    @Test
    void testFindAllClassesInPackage() {
        String packageName = "controller";
        Set<Class<?>> classes = IOUtils.findAllClassesInPackage(packageName);
        assertEquals(classes.size(), getNumOfJavaFilesInDirectory(packageName));
    }

    private int getNumOfJavaFilesInDirectory(String packageName) {
        Path path = Path.of("src/main/java", packageName);
        File directory = path.toFile();
        return (int) Arrays.stream(Objects.requireNonNull(directory.listFiles()))
                .filter(this::isJavaFile).count();
    }

    private boolean isJavaFile(File file) {
        return file.isFile() && file.getName().toLowerCase().endsWith(".java");
    }
}
