import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/***
 *A scattering read from a channel is a read operation that reads data into more than one buffer.
 * Thus, the channel "scatters" the data from the channel into multiple buffers.
 *
 * A gathering write to a channel is a write operation that writes data from more than one buffer into a single channel.
 * Thus, the channel "gathers" the data from multiple buffers into one channel.
 *
 * Scatter / gather can be really useful in situations where you need to work with various parts of the transmitted data separately.
 * For instance, if a message consists of a header and a body, you might keep the header and body in separate buffers.
 * Doing so may make it easier for you to work with header and body separately.
 *
 */

public class ScatterReadAndGatherWrite{

    public static void main(String[] args) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("./data/file.txt", "r");
        FileChannel fc = raf.getChannel();

        ByteBuffer buf1 = ByteBuffer.allocate(10);
        ByteBuffer buf2 = ByteBuffer.allocate(50);


        ByteBuffer[] bufs = {buf1, buf2};
        fc.read(bufs);  // Scatter read fc.write(bufs) would be Gather write

        buf1.flip();
        buf2.flip();

        System.out.println("Buffer 1");
        while(buf1.hasRemaining()){
            System.out.print((char) buf1.get());
        }
        System.out.println();
        System.out.println("Buffer 2");
        while(buf2.hasRemaining()){
            System.out.print((char) buf2.get());
        }

        raf.close();
    }


}
