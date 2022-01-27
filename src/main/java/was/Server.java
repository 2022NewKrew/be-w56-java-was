package was;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.*;

public class Server extends ThreadPoolExecutor {
    private static final Logger log = LoggerFactory.getLogger(Server.class);

    private final int REBUILD_THRESHOLD = 100;

    private final String host;

    private final int port;

    private final ByteBuffer buffer;

    private final ExecutorService executorPool;

    private final DispatcherServlet dispatcherServlet;

    public Server(ServerConfig serverConfig) {
        super(1, 1, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue<>());

        host = serverConfig.host;
        port = serverConfig.port;
        buffer = ByteBuffer.allocate(serverConfig.bufferSize);
        executorPool = (serverConfig.threadSize > 0) ? Executors.newFixedThreadPool(serverConfig.threadSize) : Executors.newCachedThreadPool();
        dispatcherServlet = new DispatcherServlet(serverConfig.handlers);
    }

    public void run() {
        submit(this::task);
    }

    private void task() {
        int count = 0;

        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverChannel = ServerSocketChannel.open();

            serverChannel.configureBlocking(false);
            SelectionKey serverKey = serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            serverChannel.socket().bind(new InetSocketAddress(port));

            log.info("SERVER START: " + serverKey.hashCode());

            while (true) {
                final int numOfSelectedKeys = selector.select();

                if (count >= REBUILD_THRESHOLD) {
                    selector = rebuild(selector);
                    count = 0;
                    continue;
                }

                if (numOfSelectedKeys == 0) {
                    count++;
                    continue;
                }

                Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();

                while (selectedKeys.hasNext()) {
                    SelectionKey selectedKey = selectedKeys.next();

                    if (!selectedKey.isValid()) {
                        continue;
                    }

                    if (selectedKey.isAcceptable()) {
                        accept(selector, selectedKey);
                    }

                    if (selectedKey.isReadable() && (selectedKey.interestOps() == SelectionKey.OP_READ)) {
                        read(selectedKey);
                    }
                }
                selectedKeys.remove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("SERVER END");
    }

    private Selector rebuild(Selector oldSelector) throws IOException {
        Selector newSelector = oldSelector.provider().openSelector();

        for (SelectionKey key: oldSelector.keys()) {
            if(!key.isValid()) {
                continue;
            }

            int interestOps = key.interestOps();
            key.cancel();

            key.channel().register(newSelector, interestOps);
        }

        log.info("Rebuild selector");

        return newSelector;
    }

    public void accept(Selector selector, SelectionKey serverKey) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) serverKey.channel();
        SocketChannel clientChannel;

        while ((clientChannel = serverChannel.accept()) != null) {
            clientChannel.configureBlocking(false);
            SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ);

            log.info("ACCEPT: " + clientKey.hashCode());
        }

    }

    private void read(SelectionKey key) throws IOException {

        SocketChannel clientChannel = (SocketChannel) key.channel();

        clientChannel.read(buffer);
        String rawRequest = new String(buffer.array(), 0, buffer.position());
        buffer.clear();

        log.info("READ: " + key.hashCode());

        key.interestOps(0);

        CompletableFuture.runAsync(() -> {
            try {
                write(key, dispatcherServlet.doService(rawRequest));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, executorPool);
    }

    private void write(SelectionKey key, byte[] response) throws IOException {
        try (SocketChannel clientChannel = (SocketChannel) key.channel()) {
            clientChannel.write(ByteBuffer.wrap(response));
            log.info("WRITE: " + key.hashCode());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            key.cancel();
        }
    }
}
