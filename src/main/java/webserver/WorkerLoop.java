package webserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkerLoop implements Runnable{

    private EventService eventService = new EventService();

    private int DATA_SIZE = 20000;
    ByteBuffer buffer = ByteBuffer.allocateDirect(DATA_SIZE);

    ExecutorService workers = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    final Selector selector;

    public WorkerLoop() throws IOException {
        Selector selector1;
        selector1 = selector1 = Selector.open();
        selector = selector1;
    }

    @Override
    public void run() {

        try (selector) {
            while(true) {
                // 이벤트가 발생한 Key 받기
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = keys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();
                    try {
                         if (key.isReadable() && key.interestOps() == SelectionKey.OP_READ) {
                            read(key);
                        } else if (key.isWritable()) {
                            write(key);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 비즈니스 로직 및 write 처리를 위해 Thread Pool 에 등록
    private void read(SelectionKey key) throws IOException {
        // 다시 select 되지 않도록 설정
        final int OP_IN_PROGRESS = 0;
        key.interestOps(OP_IN_PROGRESS);
        SocketChannel clientChannel = (SocketChannel) key.channel();

        String rawRequest = readBuffer(clientChannel);

//        Socket socket = clientChannel.socket();
//        socket.setKeepAlive(true);
//        socket.setSoTimeout(15 * 1000);
        Runnable t = () -> {
            try {
                eventService.doService(rawRequest, clientChannel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        workers.execute(t);
    }

    private String readBuffer(SocketChannel clientChannel) throws IOException {
        clientChannel.read(buffer);
        buffer.flip();
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        buffer.clear();
        return new String(bytes);
    }

    private void write(SelectionKey key) {}

}
