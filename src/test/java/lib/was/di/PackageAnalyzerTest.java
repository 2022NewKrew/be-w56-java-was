package lib.was.di;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class PackageAnalyzerTest {

    @Test
    void getClasses() {
        String packageName = "test.dummy";
        PackageAnalyzer analyzer = new PackageAnalyzer(mock(Logger.class));

        Class<?>[] classes = analyzer.getClasses(packageName);

        assertEquals(
                Set.of("test.dummy.Dummy1", "test.dummy.Dummy2", "test.dummy.Dummy3"),
                Arrays.stream(classes).map(Class::getName).collect(Collectors.toSet())
        );
    }
}
