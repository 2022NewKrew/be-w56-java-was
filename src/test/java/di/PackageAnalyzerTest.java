package di;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PackageAnalyzerTest {

    @Test
    void getClasses() throws IOException, ClassNotFoundException {
        String packageName = "test.dummy";

        PackageAnalyzer analyzer = new PackageAnalyzer(packageName);

        Class<?>[] classes = analyzer.getClasses();
        assertEquals(
                Set.of("test.dummy.Dummy1", "test.dummy.Dummy2", "test.dummy.Dummy3"),
                Arrays.stream(classes).map(Class::getName).collect(Collectors.toSet())
        );
    }
}
