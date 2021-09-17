package test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
/**
 * 文件拷贝各种方式的比较
 * 按字节读取
 */
public class fileTest {

    public final static String FILE_PATH ="e:\\test0.txt";
    public final static String FILE_PATH_OUT = "e:\\2.txt";

    public static void TransByCommonIoStream() throws Exception {
        long beginTime = System.currentTimeMillis();
        FileInputStream fis = new FileInputStream(new File(FILE_PATH));
        FileOutputStream fos = new FileOutputStream(new File(FILE_PATH_OUT));

        byte[] b = new byte[1024];
        int len = 0;
        while ((len = fis.read(b)) != -1) {
            //fos.write(b, 0, len);
        }

        fos.flush();
        fis.close();
        fos.close();
        long endTime = System.currentTimeMillis();
        System.out.println("采用传统IO FileInputStream 读取，耗时："
                + (endTime - beginTime));
    }

    public static void TransByCommonIoBufferedStream() throws Exception {

        long beginTime = System.currentTimeMillis();
        FileInputStream fis = new FileInputStream(new File(FILE_PATH));
        FileOutputStream fos = new FileOutputStream(new File(FILE_PATH_OUT));
        BufferedInputStream bis = new BufferedInputStream(fis);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        byte[] b = new byte[8192];
        int len = 0;
        while ((len = bis.read(b)) != -1) {
            //bos.write(b, 0, len);
        }
//        int read;
//        while ((read = bis.read()) != -1){
//            //bos.write(read);
//        }

        bos.flush();
        fis.close();
        fos.close();
        bis.close();
        bos.close();
        long endTime = System.currentTimeMillis();
        System.out.println("采用传统IO BufferedInputStream 读取，耗时："
                + (endTime - beginTime));
    }

    public static void TransByCommonIoBuffered() throws Exception {

        long beginTime = System.currentTimeMillis();
        Reader br = new BufferedReader(new FileReader(new File(FILE_PATH)));
        Writer bw = new BufferedWriter(new FileWriter(new File(FILE_PATH_OUT)));
        char[] c = new char[1024];
        int len = 0;
        while ((len = br.read(c)) != -1) {
            bw.write(c, 0, len);
        }

        bw.flush();
        br.close();
        bw.close();
        long endTime = System.currentTimeMillis();
        System.out.println("采用传统IO  BufferedReader 读取，耗时："
                + (endTime - beginTime));
    }

    public static void TransByRandomAccFile() throws Exception {

        long beginTime = System.currentTimeMillis();
        FileInputStream fis = new FileInputStream(new File(FILE_PATH));
        RandomAccessFile raf = new RandomAccessFile(new File(FILE_PATH_OUT),
                "rw");
        byte[] b = new byte[1024];
        int len = 0;
        while ((len = fis.read(b)) != -1) {
            raf.write(b, 0, len);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("采用传统IO RandomAccessFile 读取，耗时："
                + (endTime - beginTime));
    }

    /**
     * 采用FileChannel 自带方法测试 public abstract long
     * transferFrom(ReadableByteChannel src, long position, long count) throws
     * IOException;
     */
    public static void TransByNioFileChannel() throws Exception {

        long beginTime = System.currentTimeMillis();
        FileChannel fc = new FileInputStream(new File(FILE_PATH)).getChannel();
        FileChannel fco = new FileOutputStream(new File(FILE_PATH_OUT)).getChannel();
        fco.transferFrom(fc, 0, fc.size());
        long endTime = System.currentTimeMillis();
        System.out.println("采用NIO FileChannel 自带方法  读取，耗时："
                + (endTime - beginTime));
    }

    public static void TransByNioFileChannelCommon() throws Exception {

        long beginTime = System.currentTimeMillis();
        FileChannel fc = new FileInputStream(new File(FILE_PATH)).getChannel();
        FileChannel fco = new RandomAccessFile(new File(FILE_PATH_OUT), "rw")
                .getChannel();
        ByteBuffer buf = ByteBuffer.allocate(1024);

        while (fc.read(buf) != -1) {
            buf.flip();
            fco.write(buf);
            buf.clear();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("采用NIO FileChannel 循环 读取，耗时："
                + (endTime - beginTime));
    }

    public static void deleteFile() {
        File f = new File(FILE_PATH_OUT);
        if (f.exists()) {
            f.delete();
        }
    }

    /**
     * 按四字节读取
     * DataOutPutStream.readInt();
     * @throws Exception
     */
    public static void readIntFromFile(){
        long beginTime = System.currentTimeMillis();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(FILE_PATH);
            out = new FileOutputStream(FILE_PATH_OUT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        DataInputStream din = new DataInputStream(new BufferedInputStream(in));
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(out));
        long longRead = 0;
            do {
                try {
                    longRead = din.readLong();
                }catch (EOFException e){
                    System.out.println("reached the end!");
                    break;
                }catch (IOException e){
                    e.printStackTrace();
                }
                try {
                    dout.writeLong(longRead);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }while (true);
        long endTime = System.currentTimeMillis();
        System.out.printf("消耗时间:%d",endTime-beginTime);
    }

    public static void main(String[] args) throws Exception {

//        TransByCommonIoStream();
//        deleteFile();
//
//        TransByCommonIoStream();
//        deleteFile();
//        TransByCommonIoBufferedStream();
//        deleteFile();
//        TransByRandomAccFile();
//        deleteFile();
//        TransByNioFileChannel();
//        deleteFile();
//        TransByNioFileChannelCommon();
//        deleteFile();
        readIntFromFile();
        deleteFile();



    }
}

