package di;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

// https://stackoverflow.com/a/520344
public class PackageAnalyzer {

    public Class<?>[] getClasses(String packageName) throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
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

    private List<Class<?>> findClassesInDirectory(File directory, String packageName) throws ClassNotFoundException {
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

    private List<Class<?>> findClasses(File file, String packageName) throws ClassNotFoundException {
        String filename = file.getName();
        if (file.isDirectory()) {
            return findClassesInDirectory(file, packageName + "." + filename);
        }
        if (filename.endsWith(".class")) {
            String qualifier = packageName.replaceAll("^\\.", "") +
                    "." +
                    filename.replaceAll(".class$", "");
            return List.of(Class.forName(qualifier));
        }
        return Collections.emptyList();
    }
}
