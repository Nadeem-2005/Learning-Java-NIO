import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel {

    static void main(String[] args) throws IOException {

        /***
         * Before you can use a FileChannel you must open it. You cannot open a FileChannel directly. You need to obtain a FileChannel via an InputStream, OutputStream, or a RandomAccessFile. Here is how you open a FileChannel via a RandomAccessFile:
         */
        RandomAccessFile raf = new RandomAccessFile("./data/file.txt", "rw");
        FileChannel fc = raf.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(40);
        fc.read(buf);

        //reading
        while(fc.read(buf)!=-1){
            buf.flip();
            while(buf.hasRemaining()){
                System.out.print((char)buf.get());
            }
            System.out.println();
            buf.clear();
        }

        //writng
        fc.write(ByteBuffer.wrap("This line is written by a buffer.".getBytes()));

        //fc sizes and position properites
        System.out.println("File size is : " + fc.size()); //fc.size return size of the file that it is connected to.
        System.out.println("Position of fc is: "+ fc.position()); // returns at which position fc is at.

        /***
         * FileChannel Force

            The FileChannel.force() method flushes all unwritten data from the channel to the disk.
            An operating system may cache data in memory for performance reasons,
            so you are not guaranteed that data written to the channel is actually written to disk, until you call the force() method.
            The force() method takes a boolean as parameter, telling whether the file meta data (permission etc.) should be flushed too.

            Here is an example which flushes both data and meta data:
            channel.force(true);
        */

    }

}
