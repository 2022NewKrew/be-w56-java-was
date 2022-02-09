package niowebserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class RequestWorkerThread extends Thread {

    private Logger logger = LoggerFactory.getLogger(RequestWorkerThread.class);
    private Selector selector;
    private SocketChannelHandler socketChannelHandler;

    public void registerSocketChannel(SocketChannel socketChannel) throws ClosedChannelException {
        socketChannel.register(selector, SelectionKey.OP_READ);
        this.socketChannelHandler = new SocketChannelHandler();
    }

    @Override
    public void run() {
        openSelector();
        while (true) {
            try {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for(SelectionKey selectionKey : selectionKeys) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    socketChannelHandler.handle(socketChannel);
                    socketChannel.close();
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    private void openSelector() {
        try {
            this.selector = Selector.open();
        } catch (IOException e) {
            throw new WebServerException(e.getMessage(), e);
        }
    }

    private void closeSelector() {
        try {
            selector.close();
        } catch (IOException e) {
            throw new WebServerException(e.getMessage(), e);
        }
    }

    public void wakeup() {
        selector.wakeup();
    }
}
