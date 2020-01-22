package JWS;

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

    private void serve() throws IOException {
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
        System.out.println(requestString);

        HTTPRequest request = HTTPRequest.create(requestString);
        HTTPResponse response = HTTPResponse.from(request);
        System.out.println(response);
        try {
            clientChannel.write(response.getHeader());
            clientChannel.write(response.getBody());
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
