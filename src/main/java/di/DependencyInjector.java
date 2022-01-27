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
        PackageAnalyzer analyzer = new PackageAnalyzer();
        BeanParser parser = new BeanParser();
        Class<?>[] classes = analyzer.getClasses(packageName);
        BeanContainer container = parser.parse(classes);
        BeanInjector injector = new BeanInjector(container);
        injector.inject(target);
    }
}
