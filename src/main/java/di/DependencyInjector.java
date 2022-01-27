package di;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class DependencyInjector {

    public void inject(String packageName, Object target)
            throws IOException,
            ClassNotFoundException,
            IllegalAccessException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException {
        PackageAnalyzer analyzer = new PackageAnalyzer(packageName);
        BeanParser parser = new BeanParser();
        Class<?>[] classes = analyzer.getClasses();
        BeanContainer container = parser.parse(classes);
        BeanInjector injector = new BeanInjector(container);
        injector.inject(target);
    }
}
