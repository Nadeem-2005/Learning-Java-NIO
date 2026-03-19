import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class NIOSocketChannel {
    static void main(String[] args) throws IOException {
        /***A Java NIO SocketChannel is a channel that is connected to a TCP network socket.
         * It is Java NIO's equivalent of Java Networking's Sockets.
         * There are two ways a SocketChannel can be created:
         *  1. You open a SocketChannel and connect to a server somewhere on the internet.
         *  2. A SocketChannel can be created when an incoming connection arrives at a ServerSocketChannel.
         */

        //Opening a socket channel
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 9000));

        //closing a socketchannel
        socketChannel.close();

        //Reding and Writing are both same as other channels
    }
}
