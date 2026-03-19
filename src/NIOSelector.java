import java.io.IOException;

import java.net.SocketException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/***
 * NOTE:- Only channels that extend SelectableChannel (network channels) can be used with a selector. FileChannel cant use SelectableChannel
 *
 * The Java NIO Selector is a component which can examine one or more Java NIO Channel instances,
 * and determine which channels are ready for e.g. reading or writing. This way a single thread can manage multiple channels,
 * and thus multiple network connections.
 */

public class NIOSelector {
    static void main(String[] args) throws IOException {
        Selector selector = Selector.open();

        SocketChannel socketChannel = SocketChannel.open(),socketChannel2 = SocketChannel.open();
        socketChannel.configureBlocking(false); socketChannel2.configureBlocking(false);


        /***
         * The Channel must be in non-blocking mode to be used with a Selector.
         * This means that you cannot use FileChannel's with a Selector since FileChannel's cannot be switched into non-blocking mode.
         * Socket channels will work fine though.
         *
         * Notice the second parameter of the register() method.
         * This is an "interest set", meaning what events you are interested in listening for in the Channel, via the Selector.
         * There are four different events you can listen for:
         *
         *      Connect
         *      Accept
         *      Read
         *      Write
         *
         * A channel that "fires an event" is also said to be "ready" for that event.
         * So, a channel that has connected successfully to another server is "connect ready".
         * A server socket channel which accepts an incoming connection is "accept" ready.
         * A channel that has data ready to be read is "read" ready.
         * A channel that is ready for you to write data to it, is "write" ready.
         *
         * These four events are represented by the four SelectionKey constants:
         *      SelectionKey.OP_CONNECT
         *      SelectionKey.OP_ACCEPT
         *      SelectionKey.OP_READ
         *      SelectionKey.OP_WRITE
         */

        SelectionKey key = socketChannel.register(selector, SelectionKey.OP_READ);
//        For multiple events such as Connecting or Reading
        SelectionKey key2 = socketChannel2.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);

        selector.select();

//        Ready set can be accessed by the following:
        int readyOps = key.readyOps();
        int readyOps2 = key2.readyOps();
        System.out.println("readyOps: " + readyOps + " readyOps2: " + readyOps2);


    }
}
