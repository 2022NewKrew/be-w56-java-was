package was.domain.event;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.*;

public class EventLoop extends ThreadPoolExecutor {
    private final EventService eventService;
    private final ByteBuffer buffer = ByteBuffer.allocate(26214400);

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final int port;

    public EventLoop(int port, EventService eventService) {
        super(1, 1, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue<>());
        this.eventService = eventService;
        this.port = port;
    }

    public void run() {
        submit(this::task);
    }

    private void task() {
        try (final Selector selector = Selector.open();
             final ServerSocketChannel serverChannel = ServerSocketChannel.open()) {

            serverChannel.configureBlocking(false);
            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                final int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }

                final Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    final SelectionKey key = keys.next();

                    if (!key.isValid()) {
                        continue;
                    }

                    if (key.isAcceptable()) {
                        register(key);
                    }

                    if (key.isReadable() && (key.interestOps() == SelectionKey.OP_READ)) {
                        doService(key);
                    }

                    keys.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doService(SelectionKey key) {
        changeOps(key);

        final SocketChannel client = (SocketChannel) key.channel();
        final byte[] requestBytes;

        try {
            requestBytes = readBuffer(client);

            if (requestBytes[0] == '\0') return;

            CompletableFuture.runAsync(doService(client, requestBytes), executorService)
                    .exceptionally((ex) -> {
                        ex.printStackTrace();
                        return null;
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Runnable doService(SocketChannel client, byte[] requestBytes) {
        return () -> eventService.doService(requestBytes, result -> returnResponse(client, result));
    }

    private void changeOps(SelectionKey key) {
        final int OP_IN_PROGRESS = 0;
        key.interestOps(OP_IN_PROGRESS);
    }

    private byte[] readBuffer(SocketChannel client) throws IOException {
        client.read(buffer);
        buffer.flip();

        final byte[] requestMsg = new byte[buffer.limit()];
        buffer.get(requestMsg);

        buffer.clear();
        return requestMsg;
    }

    private void returnResponse(SocketChannel client, byte[] result) {
        try {
            final ByteBuffer buffer = ByteBuffer.wrap(result);
            while (buffer.hasRemaining()) {
                client.write(buffer);
            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void register(SelectionKey key) throws IOException {
        final ServerSocketChannel severChannel = ((ServerSocketChannel) key.channel());
        final SocketChannel client = severChannel.accept();
        client.configureBlocking(false);
        client.register(key.selector(), SelectionKey.OP_READ);
    }
}
