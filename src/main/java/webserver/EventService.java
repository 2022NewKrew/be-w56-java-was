package webserver;

import framework.FrontController;
import framework.config.ServerConfig;
import util.HttpRequest;
import util.HttpResponse;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class EventService {

    // data buffer size
    private int DATA_SIZE = 2000000;
    // handler 를 매핑하고 HttpResponse 를 생성하는 FrontController
    private final FrontController frontController = new FrontController(ServerConfig.handlerMapping, ServerConfig.viewResolver);

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
            // buffer 에서 요청을 읽어들인 다음 HttpRequest 와 HttpResponse 를 생성
            req = new HttpRequest(rawRequest);
            res = new HttpResponse(req);

            frontController.doDispatch(req, res);
        } finally {
            // HttpResponse 를 byte로 변환하고 write
            ByteBuffer writeBuffer = ByteBuffer.wrap(res.toByte());
            while (writeBuffer.hasRemaining()) {
                clientChannel.write(writeBuffer);
            }
            // SocketChannel 을 닫는다
            clientChannel.close();
        }

    }
}
