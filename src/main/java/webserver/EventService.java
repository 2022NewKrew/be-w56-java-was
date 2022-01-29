package webserver;

import framework.RequestDispatcher;
import framework.config.ServerConfig;
import util.HttpRequest;
import util.HttpResponse;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class EventService {

    private int DATA_SIZE = 2000000;
    private final RequestDispatcher requestDispatcher = new RequestDispatcher(ServerConfig.handlerMapping, ServerConfig.viewResolver);

    public void doService(SocketChannel clientChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(DATA_SIZE);
        HttpRequest req = null;
        HttpResponse res = null;

        try {
            clientChannel.read(buffer);
            buffer.flip();
            byte[] bytes = new byte[buffer.limit()];
            buffer.get(bytes);
            String rawRequest = new String(bytes);
            req = new HttpRequest(rawRequest);
            res = new HttpResponse(req);

            requestDispatcher.doDispatch(req, res);
        } finally {
            ByteBuffer writeBuffer = ByteBuffer.wrap(res.toByte());
            while (writeBuffer.hasRemaining()) {
                clientChannel.write(writeBuffer);
            }
            clientChannel.close();
        }

    }
}
