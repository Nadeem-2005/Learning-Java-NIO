import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/***
 * NOTE:- Only channels that extend SelectableChannel (network channels) can be used with a selector. FileChannel cant use SelectableChannel
 *
 * The Java NIO Selector is a component which can examine one or more Java NIO Channel instances,
 * and determine which channels are ready for e.g. reading or writing. This way a single thread can manage multiple channels,
 * and thus multiple network connections.
 */

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

public class NIOSelector {
    static void main(String[] args) throws IOException {
        Selector selector = Selector.open();

        //Creating a server channel
        ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.bind(new InetSocketAddress("localhost", 9000));
        server.register(selector, SelectionKey.OP_ACCEPT);

        //creating a client
        SocketChannel client = SocketChannel.open();
        client.configureBlocking(false);
        client.connect(new InetSocketAddress("localhost", 9000));
        client.register(selector, SelectionKey.OP_CONNECT);

        //event loop
        while(true){
            selector.select(); //blocks until one channel is ready

            //selector.selectedKeys will return a set of keys that are associated with that particular selector that are ready for an event
            //for example, even if a selector is to monitor a 1000 channels, if only 3 are ready for event, then the size of the set will be 3.
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();

            while(iter.hasNext()){
                SelectionKey key = iter.next();
                iter.remove();

                if(key.isAcceptable()){
                    // if a key is ready to accept event (i.e.., a server), then we expose a new channel for our client to write into
                    SocketChannel writeChannel = server.accept();
                    writeChannel.configureBlocking(false);
                    writeChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("Server has accepted a new connection from " + writeChannel.getRemoteAddress());

                }
                else if(key.isConnectable()){
                    //if a key is ready for a connection event , then
                    // getting the channel that is ready to establish a connection which in our case, is our single client
                    SocketChannel readyClient = (SocketChannel) key.channel();
                    //finishes connection handshake
                    readyClient.finishConnect();
                    // once client is connected to server, switching it to write mode so that it can write to the channel exposed by the server
                    key.interestOps(SelectionKey.OP_WRITE);

                    System.out.println("Client has connected succesfully");
                }
                else if (key.isWritable()){
                    SocketChannel readyClient = (SocketChannel) key.channel();
                    //forms a tcp connection and automatically writes to writeChannel exposed by server.
                    readyClient.write(ByteBuffer.wrap("Hello server, Client here.".getBytes()));

                    //the client is put into read event because, if this isnt done so, the selectro keeps thinking it wants to write
                    key.interestOps(SelectionKey.OP_READ);
                    System.out.println("Client has sent a message.");
                }
                else if (key.isReadable()){
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    int bytes = channel.read(readBuffer);
                    if (bytes > 0){
                        readBuffer.flip();

                        while(readBuffer.hasRemaining()){
                            System.out.print((char)readBuffer.get());
                        }
                    }
                    key.cancel();
                    selector.close();
                    server.close();
                    return;

                }
            }
        }
    }
}
