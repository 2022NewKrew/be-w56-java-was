package webserver;

import annotation.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ClassUtils;
import webserver.method.MysqlDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Properties;
import java.util.Set;

public class WebServerConfiguration {
    private static final Logger log = LoggerFactory.getLogger(WebServerConfiguration.class);

    public static void doConfigure() {
        initializeProperties();
        initializeStereoTypes();
        initializeDispatcherServlet();
        initializeVaultManager();
        initializeDataSource();
    }

    private static void initializeProperties() {
        Properties properties = new Properties();
        String propertiesFilePath = "src/main/resources/webserver.properties";
        try {
            properties.load(new FileInputStream(propertiesFilePath));
        } catch (IOException e) {
            log.error("File not found: " + propertiesFilePath);
        }
        SingletonBeanRegistry.registerBean("Properties", properties);
    }

    private static void initializeStereoTypes() {
        registerAnnotatedClasses(Controller.class);
    }

    private static void registerAnnotatedClasses(Class<? extends Annotation> annotation) {
        Set<Class<?>> classes = ClassUtils.findAnnotatedClasses(annotation);
        classes.forEach(WebServerConfiguration::registerClass);
    }

    private static void registerClass(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            Object instance = constructor.newInstance();
            SingletonBeanRegistry.registerBean(clazz.getName(), instance);
        } catch (ReflectiveOperationException e) {
            log.error("Failed to register class of " + clazz.getName());
        }
    }

    private static void initializeDispatcherServlet() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.initialize();
        SingletonBeanRegistry.registerBean("DispatcherServlet", dispatcherServlet);
    }

    private static void initializeVaultManager() {
        VaultManager vaultManager = new VaultManager();
        vaultManager.initialize();
        SingletonBeanRegistry.registerBean("VaultManager", vaultManager);
    }

    private static void initializeDataSource() {
        MysqlDataSourceFactory dataSourceFactory = new MysqlDataSourceFactory();
        DataSource dataSource = dataSourceFactory.getDataSource();
        SingletonBeanRegistry.registerBean("DataSource", dataSource);
    }
}
