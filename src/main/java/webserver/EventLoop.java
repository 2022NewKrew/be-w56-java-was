package webserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventLoop {

    Selector selector;
    ServerSocketChannel serverSocketChannel;

    ExecutorService workers = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    EventService eventService = new EventService();

    public void startLoop(int port) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while(true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();

            Iterator<SelectionKey> keyIterator = keys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();
                try {
                    if (key.isAcceptable()) {
                        accept(key);
                    } else if (key.isReadable() && key.interestOps() == SelectionKey.OP_READ) {
                        read(key);
                    } else if (key.isWritable()) {
                        write(key);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) {
        final int OP_IN_PROGRESS = 0;
        key.interestOps(OP_IN_PROGRESS);
        SocketChannel clientChannel = (SocketChannel) key.channel();
        Runnable t = () -> {
            try {
                eventService.doService(clientChannel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        workers.execute(t);
    }

    private void write(SelectionKey key) {}

}
