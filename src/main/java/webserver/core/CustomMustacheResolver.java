package webserver.core;

import com.github.mustachejava.MustacheResolver;
import com.github.mustachejava.resolver.FileSystemResolver;


import java.io.File;
import java.io.Reader;

public class CustomMustacheResolver implements MustacheResolver {

    private final FileSystemResolver fileSystemResolver;

    public CustomMustacheResolver(File fileRoot) {
        this.fileSystemResolver = new FileSystemResolver(fileRoot);
    }

    @Override
    public Reader getReader(String resourceName) {
        if (!resourceName.contains(".html")) resourceName = resourceName + ".html";
        return fileSystemResolver.getReader(resourceName);
    }

}
