package lib.was.di;

import org.slf4j.Logger;

public class DependencyInjector {

    private final PackageAnalyzer analyzer;
    private final BeanParser parser;
    private final BeanInjector injector;

    public DependencyInjector(Logger logger) {
        analyzer = new PackageAnalyzer(logger);
        parser = new BeanParser();
        injector = new BeanInjector(logger);
    }

    public void inject(String packageName, Object target) {
        Class<?>[] classes = analyzer.getClasses(packageName);
        BeanContainer container = parser.parse(classes);
        injector.inject(container, target);
    }
}
