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

    // 비즈니스 로직 및 Socket write 담당 고정 Thread Pool : 사용 가능 코어 개수
    ExecutorService workers = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    EventService eventService = new EventService();

    public void startLoop(int port) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        // non-blocking 설정
        serverSocketChannel.configureBlocking(false);
        // port(8080) 설정
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        // socketChannel 을 selector 에 OP_ACCEPT 로 등록
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while(true) {
            // 이벤트가 발생한 Key 받기
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

    // SocketChannel 을 받아서 selector 에 OP_READ 로 등록
    private void accept(SelectionKey key) throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    // 비즈니스 로직 및 write 처리를 위해 Thread Pool 에 등록
    private void read(SelectionKey key) {
        // 다시 select 되지 않도록 설정
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
