package webserver;

import framework.FrontController;
import framework.config.ServerConfig;
import util.HttpRequest;
import util.HttpResponse;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class EventService {

    // handler 를 매핑하고 HttpResponse 를 생성하는 FrontController
    private final FrontController frontController = new FrontController(ServerConfig.handlerMapping, ServerConfig.viewResolver);

    public void doService(String rawRequest, SocketChannel clientChannel) throws IOException {
        HttpRequest req = null;
        HttpResponse res = null;

        try {
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
//            clientChannel.close();
        }

    }
}
