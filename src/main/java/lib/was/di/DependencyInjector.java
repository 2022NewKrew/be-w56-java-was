package lib.was.di;

public class DependencyInjector {

    private final PackageAnalyzer analyzer;
    private final BeanParser parser;
    private final BeanInjector injector;

    public DependencyInjector() {
        analyzer = new PackageAnalyzer();
        parser = new BeanParser();
        injector = new BeanInjector();
    }

    public void inject(String packageName, Object target) {
        Class<?>[] classes = analyzer.getClasses(packageName);
        BeanContainer container = parser.parse(classes);
        injector.inject(container, target);
    }
}
