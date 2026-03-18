/***
 * Tutorial from https://jenkov.com/tutorials/java-nio/index.html
 *
 * Channels can read-into buffers and Channels can write-from a buffer
 * Types of Channels:
 *      FileChannel -> reads data from and to files
 *      DatagramChannel -> read and write data over network via UDP
 *      SocketChannel -> read and write data over network via TCP
 *      SeverSocketChannel -> allows you to listen for incoming TCP connections, like a web server does. For each incoming connection a SocketChannel is created.
 *
 *  Intuition (Super Important)
 * 	    •	Channel = highway/pipe
 * 	    •	Buffer = vehicle carrying data/bucket
 * 	    •	Data always travels inside the vehicle, not directly on the road
 * 	    Therefore :
 * 	        Read-into Buffer: filling the bucket from the pipe
 * 	        Write-from Buffer: pour water from bucket into pipe
 *
 * Basic Buffer Usage
 *Using a Buffer to read and write data typically follows this little 4-step process:
 *      Write data into the Buffer
 *      Call buffer.flip()
 *      Read data out of the Buffer
 *      Call buffer.clear() or buffer.compact()
 *
 * Java NIO comes with the following Buffer types:
 *
 *      ByteBuffer
 *      MappedByteBuffer
 *      CharBuffer
 *      DoubleBuffer
 *      FloatBuffer
 *      IntBuffer
 *      LongBuffer
 *      ShortBuffer
 *
 * A scattering read from a channel is a read operation that reads data into more than one buffer.
 * Thus, the channel "scatters" the data from the channel into multiple buffers.
 *
 * A gathering write to a channel is a write operation that writes data from more than one buffer into a single channel.
 * Thus, the channel "gathers" the data from multiple buffers into one channel.

 */

void main() {
    try{
        RandomAccessFile ref = new RandomAccessFile("./data/file.txt", "rw");
        FileChannel fc = ref.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(10);

        int nosOfBytes = fc.read(buf);
        while(nosOfBytes != -1){
            System.out.println("Bytes read: "+ nosOfBytes);
            buf.flip();

            while(buf.hasRemaining()){
                System.out.print((char) buf.get());
            }
            buf.clear();
            System.out.println();
            nosOfBytes = fc.read(buf);
        }

        ref.close();
    }
    catch (Exception e){
        e.printStackTrace();
    }
}