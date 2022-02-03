package was.domain.eventLoop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class WorkerEventLoop implements EventLoop {

    private final Logger logger = LoggerFactory.getLogger(WorkerEventLoop.class);

    private final ByteBuffer buffer = ByteBuffer.allocateDirect(2);

    private final EventService eventService;
    private final Selector selector;

    public WorkerEventLoop(EventService eventService) {
        this.eventService = eventService;
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void run() {
        try (selector) {
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

                    if (key.isReadable()) {
                        process(key);
                    }

                    keys.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void process(SelectionKey key) {
        final SocketChannel client = (SocketChannel) key.channel();
        final byte[] requestBytes = readBuffer(client);

        if (isKeepAlive(client, requestBytes)) {
            key.interestOps(0);
            return;
        }

        if (isClosed(client, requestBytes)) {
            closeSocket(client);
            return;
        }

        final byte[] responseBytes = eventService.doService(requestBytes);
        returnResponse(client, responseBytes);
    }

    private boolean isClosed(SocketChannel client, byte[] requestBytes) {
        return requestBytes == null && !client.isConnected();
    }

    private boolean isKeepAlive(SocketChannel client, byte[] requestBytes) {
        return requestBytes == null && client.isConnected();
    }

    private void closeSocket(SocketChannel client) {
        try {
            logger.info("closed");
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] readBuffer(SocketChannel client) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int numOfBytes = 0;
        try {
            while ((numOfBytes = client.read(buffer)) > 0 || buffer.position() > 0) {
                buffer.flip();
                final byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                byteArrayOutputStream.write(bytes);
                buffer.clear();
            }
        } catch (IOException e) {
            closeSocket(client);
            e.printStackTrace();
        } finally {
            buffer.clear();
        }

        if (numOfBytes == -1) {
            return null;
        }

        return byteArrayOutputStream.toByteArray();
    }

    private void returnResponse(SocketChannel client, byte[] result) {
        final ByteBuffer buffer = ByteBuffer.wrap(result);

        try {
            while (buffer.hasRemaining()) {
                client.write(buffer);
            }
        } catch (IOException e) {
            closeSocket(client);
            e.printStackTrace();
        }
    }

    public void register(SocketChannel client) {
        try {
            client.configureBlocking(false);
            client.socket().setKeepAlive(true);
            client.socket().setSoTimeout(15 * 1000);
            client.register(selector, SelectionKey.OP_READ);

            selector.wakeup();
        } catch (IOException e) {
            closeSocket(client);
            e.printStackTrace();
        }
    }
}
