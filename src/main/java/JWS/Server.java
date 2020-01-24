package JWS;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class Server {

    private boolean running = true;
    private static final Logger log = LogManager.getLogger(Server.class);

    void serve() throws IOException {
        log.info("Server starting..");

        int clientMessageReadLength = 32 * 1024;
        Charset charset = StandardCharsets.ISO_8859_1;

        Selector selector = Selector.open();
        ServerSocketChannel channel = ServerSocketChannel.open();

        channel.bind(new java.net.InetSocketAddress(8080));
        channel.configureBlocking(false);

        SelectionKey serverChannel = channel.register(selector, SelectionKey.OP_ACCEPT);

        while (running) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            for (SelectionKey key : keys) {
                if (key == serverChannel) {
                    if (key.isAcceptable()) {
                        SocketChannel client = channel.accept();

                        if (client == null)
                            continue;

                        client.configureBlocking(false);
                        client.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
                        client.register(selector, SelectionKey.OP_READ);
                    }
                }
                else if (key.isReadable()) {
                    SocketChannel clientChannel = (SocketChannel) key.channel();

                    ByteBuffer clientMessage = ByteBuffer.allocate(clientMessageReadLength);
                    clientChannel.read(clientMessage);

                    String request = new String(clientMessage.array(), charset).trim();
                    handleRequest(request, clientChannel);
                }
            }
        }
    }

    private void handleRequest(String requestString, SocketChannel clientChannel) {
        log.debug("Request string:\n{}", requestString);

        HTTPRequest request = HTTPRequest.create(requestString);
        HTTPResponse response = HTTPResponse.from(request);
        log.debug("Response:\n{}", response);
        try {
            clientChannel.write(response.getHeaderAsByteBuffer());
            clientChannel.write(response.getBodyAsByteBuffer());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
