package niowebserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpFactory;
import webserver.http.exception.ExceptionResolver;
import webserver.processor.handler.controller.Controller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Set;

public class NioWebServer {
    private static final Logger log = LoggerFactory.getLogger(NioWebServer.class);
    private final int listenPort;
    private final RequestWorkerThreadPool requestWorkerThreadPool;

    public NioWebServer(List<Controller> controllers, List<ExceptionResolver> exceptionResolvers, int threadSize, int port) {
        this.requestWorkerThreadPool = new RequestWorkerThreadPool(threadSize);
        this.listenPort = port;
        HttpFactory.initialize(controllers, exceptionResolvers);
    }

    public void start() {
        try (Selector selector = Selector.open(); ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()){
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(listenPort));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while(true) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for(SelectionKey selectionKey : selectionKeys) {
                    SocketChannel socketChannel = getChannelByKey(selectionKey);
                    if(socketChannel == null) {
                        selectionKey.cancel();
                        continue;
                    }
                    socketChannel.configureBlocking(false);
                    requestWorkerThreadPool.registerSocketChannel(socketChannel);
                }
            }
        } catch (Exception e) {
            log.error("Error : ", e);
        }
    }

    private SocketChannel getChannelByKey(SelectionKey selectionKey) {
        try {
            if (selectionKey.isAcceptable()) {
                ServerSocketChannel socketChannel = (ServerSocketChannel) selectionKey.channel();
                return socketChannel.accept();
            }
            throw new WebServerException("Socket is not Acceptable");
        } catch (IOException e) {
            throw new WebServerException(e.getMessage(), e);
        }
    }
}
