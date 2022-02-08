package was;

import application.config.WebConfig;
import di.BeanContext;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationBuilder {
    private static final ApplicationBuilder builder = new ApplicationBuilder();

    public static ApplicationBuilder builder() {
        return builder;
    }

    private final List<String> packageNames = new ArrayList<>();
    private WebConfig webConfig;

    public ApplicationBuilder setSeverConfig(WebConfig webConfig) {
        this.webConfig = webConfig;
        return this;
    }

    public ApplicationBuilder addPackageNameForBeanScan(String packageName) {
        packageNames.add(packageName);
        return this;
    }

    public NioWebApplicationServer build() {
        final BeanContext beanContext = BeanContext.getInstance();

        for (String packageName : packageNames) {
            try {
                beanContext.make(packageName);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return new NioWebApplicationServer(webConfig);
    }
}
