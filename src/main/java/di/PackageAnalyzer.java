package di;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

// https://stackoverflow.com/a/520344
public class PackageAnalyzer {

    private final String packageName;

    public PackageAnalyzer(String packageName) {
        this.packageName = packageName;
    }

    public Class<?>[] getClasses() throws IOException, ClassNotFoundException {
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
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class<?>[] {});
    }

    private List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files != null ? files : new File[0]) {
            String filename = file.getName();
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + filename));
                continue;
            }
            if (filename.endsWith(".class")) {
                String qualifier = packageName.replaceAll("^\\.", "") +
                        "." +
                        filename.replaceAll(".class$", "");
                classes.add(Class.forName(qualifier));
            }
        }
        return classes;
    }
}
