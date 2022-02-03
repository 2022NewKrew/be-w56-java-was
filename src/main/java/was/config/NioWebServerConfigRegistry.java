package was.config;

import was.domain.http.MethodAndPath;
import was.meta.HttpMethod;

import java.util.HashMap;
import java.util.Map;

public class NioWebServerConfigRegistry {
    private int port = 8080;
    private int workerEventLoopSize = 1;

    public void setPort(int port) {
        this.port = port;
    }

    public void setWorkerEventLoopSize(int workerEventLoopSize) {
        this.workerEventLoopSize = workerEventLoopSize;
    }

    public int getWorkerEventLoopSize() {
        return workerEventLoopSize;
    }

    public int getPort() {
        return port;
    }
}
