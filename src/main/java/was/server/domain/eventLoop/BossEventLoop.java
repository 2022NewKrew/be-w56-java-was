package was.server.domain.eventLoop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.*;
import java.util.function.Consumer;

public class BossEventLoop implements EventLoop {

    private final Logger logger = LoggerFactory.getLogger(BossEventLoop.class);

    private final int port;
    private final Consumer<SelectionKey> registerAtWorkerEventLoop;

    public BossEventLoop(int port, Consumer<SelectionKey> registerAtWorkerEventLoop) {
        this.port = port;
        this.registerAtWorkerEventLoop = registerAtWorkerEventLoop;
    }

    @Override
    public void run() {
        try (final Selector selector = Selector.open();
             final ServerSocketChannel serverChannel = ServerSocketChannel.open()) {

            serverChannel.configureBlocking(false);
            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.register(selector, SelectionKey.OP_ACCEPT, registerAtWorkerEventLoop);

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
                        registerAtWorkerEventLoop.accept(key);
                    }

                    keys.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
