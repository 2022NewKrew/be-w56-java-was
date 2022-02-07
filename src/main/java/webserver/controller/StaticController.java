package webserver.controller;

import java.io.File;
import java.util.*;

public class StaticController extends Controller {

    public StaticController() {
        Set<String> files = new HashSet<>();
        File file = new File("./webapp");
        Queue<File> queue = new LinkedList<>(Arrays.asList(Objects.requireNonNull(file.listFiles())));
        while(!queue.isEmpty()){
            File f = queue.poll();
            if (f.isDirectory()){
                queue.addAll(Arrays.asList(Objects.requireNonNull(f.listFiles())));
                continue;
            }
            files.add(f.getPath().substring(8));
        }

//        files.stream().forEach(f -> runner.put(new MethodAndUrl(HttpMethod.GET, f), findStaticFile));
    }
}
