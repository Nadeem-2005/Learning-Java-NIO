import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NIOServerSocketChannel {

    static void main(String[] args) throws IOException {

        //opening a server socket channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 9000));


        //listening for connections
        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            //do something with this
            break;
        }

        //closing a server socket channel
        serverSocketChannel.close();
    }


}
