package niowebserver;

import http.HttpRequest;
import http.HttpRequestParser;
import http.HttpResponse;
import http.HttpResponseRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpFactory;
import webserver.processor.HttpProcessor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class SocketChannelHandler {

    private Logger logger = LoggerFactory.getLogger(SocketChannelHandler.class);
    private final ByteBuffer buffer;

    public SocketChannelHandler() {
        this.buffer = ByteBuffer.allocate(1024);
    }

    public void handle(SocketChannel socketChannel) throws IOException {
        this.buffer.clear();
        HttpRequest httpRequest = parseHttpRequest(socketChannel);
        HttpProcessor httpProcessor = HttpFactory.httpProcessor();
        HttpResponse response = httpProcessor.process(httpRequest);
        ByteArrayOutputStream byteArrayOutputStream = renderOutputStream(response);
        ByteBuffer writeBuffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        socketChannel.write(writeBuffer);
    }

    private HttpRequest parseHttpRequest(SocketChannel socketChannel) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int readBytes;
        while(true) {
            readBytes = socketChannel.read(buffer);
            if(readBytes == 0 || readBytes == -1) {
                break;
            }
            buffer.flip();
            byte[] bytes = buffer.array();
            bos.write(bytes, 0, buffer.limit());
            buffer.clear();
        }
        if(readBytes == -1) {
            socketChannel.close();
        }
        HttpRequestParser requestParser = HttpFactory.httpRequestParser();
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        return requestParser.parse(bis);
    }

    private ByteArrayOutputStream renderOutputStream(HttpResponse httpResponse) {
        HttpResponseRenderer httpResponseRenderer = HttpFactory.httpResponseRenderer();
        return httpResponseRenderer.render(httpResponse);
    }
}
