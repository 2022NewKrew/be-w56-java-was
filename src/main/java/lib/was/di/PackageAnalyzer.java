package lib.was.di;

import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

// https://stackoverflow.com/a/520344
public class PackageAnalyzer {

    private final Logger logger;

    public PackageAnalyzer(Logger logger) {
        this.logger = logger;
    }

    public Class<?>[] getClasses(String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources(path);
        } catch (IOException e) {
            logger.error("Error loading classes from package {}", packageName, e);
            return new Class<?>[0];
        }
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            List<Class<?>> found = findClassesInDirectory(directory, packageName);
            classes.addAll(found);
        }
        return classes.toArray(new Class<?>[] {});
    }

    private List<Class<?>> findClassesInDirectory(File directory, String packageName) {
        if (!directory.exists()) {
            return Collections.emptyList();
        }
        List<Class<?>> classes = new ArrayList<>();
        File[] files = directory.listFiles();
        for (File file : files != null ? files : new File[0]) {
            List<Class<?>> found = findClasses(file, packageName);
            classes.addAll(found);
        }
        return classes;
    }

    private List<Class<?>> findClasses(File file, String packageName) {
        String filename = file.getName();
        if (file.isDirectory()) {
            return findClassesInDirectory(file, packageName + "." + filename);
        }
        if (filename.endsWith(".class")) {
            String qualifier = packageName.replaceAll("^\\.", "") +
                    "." +
                    filename.replaceAll(".class$", "");
            return findClass(qualifier);
        }
        return Collections.emptyList();
    }

    private List<Class<?>> findClass(String qualifier) {
        try {
            return List.of(Class.forName(qualifier));
        } catch (ClassNotFoundException e) {
            logger.error("Error loading class {}", qualifier, e);
            return Collections.emptyList();
        }
    }
}
