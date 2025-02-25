package com.example.androidmodel.tools.dexfix.simple.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * @author kfflso
 * @data 2024/10/16 14:07
 * @plus:
 */
public class DexUtils {

    /**
     * 从 base 的 offset 开始读取 len 长度的数据, 从 dest 的 offset2 开始覆盖写入 dest。
     * 如果 dest 超出容量，则自动扩容。
     *
     * @param base   源 ByteBuffer
     * @param offset 从 base 中读取数据的起始偏移量
     * @param len    从 base 中读取的字节长度
     * @param dest   目标 ByteBuffer
     * @param offset2 在 dest 中写入数据的起始偏移量
     * @return 是否成功写入数据
     */
    public boolean writeByteBufferToBB(ByteBuffer base, int offset, int len, ByteBuffer dest, int offset2) {
        if (base == null || offset < 0 || len <= 0 || offset2 < 0) {
            return false;
        }
        if (offset + len > base.capacity()) {
            return false;
        }
        int destNeededCapacity = (offset2 + len);
        if(dest == null){
            dest = ByteBuffer.allocate(destNeededCapacity);
        }else {
            //dest 扩容v
            if (destNeededCapacity > dest.capacity()) {
                ByteBuffer newDest = ByteBuffer.allocate(destNeededCapacity);
                dest.flip();
                newDest.put(dest);
                dest = newDest;
            }
        }
        // 从 base 中读取数据并写入 dest
        try {
            base.position(offset);
            byte[] data = new byte[len];
            base.get(data, 0, len);
            dest.position(offset2);
            dest.put(data);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将 bytes 从 dest offset 开始全部写入;
     * @param bytes
     * @param dest
     * @param offset
     * @return
     */
    public boolean writeBytesToBB(byte[] bytes, ByteBuffer dest, int offset) {
        if (bytes == null || offset < 0 ) {
            return false;
        }
        int neededCapacity = offset + bytes.length;
        if(dest == null){
            dest = ByteBuffer.allocate(neededCapacity);
        }else {
             if(neededCapacity > dest.capacity()){
                 ByteBuffer newBuffer = ByteBuffer.allocate(neededCapacity);
                 dest.flip();// 反转当前缓冲区以便从头开始写入
                 newBuffer.put(dest);
                 dest = newBuffer;
             }
        }

        dest.position(offset);
        dest.put(bytes);
        return true;
    }



    public byte[] readByteBufferToByte(ByteBuffer buffer, int offset, int len){
        if(buffer==null || offset < 0 || len <= 0 || (buffer.capacity() < (offset+len)) ){
            return null ;
        }
        buffer.position(offset);
        byte[] bytes = new byte[len];
        buffer.get(bytes,offset,len);
        return bytes;
    }

    public int readInt(ByteBuffer buffer) {
        return buffer.getInt();
    }

    public short readShort(ByteBuffer buffer) {
        return buffer.getShort();
    }
    public short readShort(ByteBuffer buffer,int offset) {
        buffer.position(offset);
        return buffer.getShort();
    }

    public int readUnsignedShort(ByteBuffer buffer) {
        return readShort(buffer) & 0xffff;
    }
    public int readUnsignedShort(ByteBuffer buffer,int offset) {
        return readShort(buffer,offset) & 0xffff;
    }
    public byte readByte(ByteBuffer buffer) {
        return buffer.get();
    }
    public byte[] readByteArray(ByteBuffer buffer, int length) {
        byte[] result = new byte[length];
        buffer.get(result);
        return result;
    }






    public long getMethodNextOffset(long offset, RandomAccessFile raf){
        long len = getMethodLength(offset,raf);
        return offset + len;
    }
    public long getMethodLength(long offset, RandomAccessFile raf){
        long len = 0;
        try {
            //raf 当前seek的位置
//            long cur = raf.
            raf.seek(offset);
            int registersSize = raf.readUnsignedShort();
            int insSize = raf.readUnsignedShort();
            int outsSize = raf.readUnsignedShort();
            int triesSize = raf.readUnsignedShort();
            int debugInfoOffset = raf.readInt();
            int instructionsSize = raf.readInt();
            short[] instructions = readShortArray(raf,instructionsSize);

            int instructionsLength = instructionsSize * 2;
            int padding = 0;
            if(triesSize > 0 && instructionsLength % 4 != 0){
                padding = 2;
            }

            len = 16 + instructionsLength + padding;

            //恢复raf seek 的位置;
            raf.seek(offset);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return len;
    }
    private short[] readShortArray(RandomAccessFile raf, int size){
        short[] array = new short[size];
        try {
            for(int i=0; i<size; i++){
                array[i] = raf.readShort();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return array;
    }

    /**
     * 将rsf_in中从in_offset开始的in_len字节写入到rsf_dest中
     *
     * @param rsf_dest   目标文件的RandomAccessFile
     * @param rsf_in     源文件的RandomAccessFile
     * @param in_offset  源文件的偏移量
     * @param in_len     要读取的字节长度
     * @throws IOException 可能抛出的IO异常
     */
    public void write(RandomAccessFile rsf_dest, RandomAccessFile rsf_in, long in_offset, int in_len) throws IOException {
        if (in_offset < 0 || in_len < 0) {
            return;
        }
        rsf_in.seek(in_offset);
        byte[] buffer = new byte[4096];
        int bytesRead;
        int totalBytesRead = 0;
        while (totalBytesRead < in_len) {
            int bytesToRead = Math.min(buffer.length, in_len - totalBytesRead);
            // 从rsf_in读取数据到buffer
            bytesRead = rsf_in.read(buffer, 0, bytesToRead);
            // 检查是否已经读完文件
            if (bytesRead == -1) {
                break;
            }
            // 将读取到的数据写入rsf_dest
            rsf_dest.write(buffer, 0, bytesRead);
            // 更新已读取的总字节数
            totalBytesRead += bytesRead;
        }
    }

    /**
     * 将bytes的数据从offset开始,覆盖的写入raf
     *
     * @param raf    目标文件的RandomAccessFile
     * @param offset 写入的起始偏移量
     * @param bytes  要写入的字节数组
     */
    public void write(RandomAccessFile raf, long offset, byte[] bytes) {
        try {
            // 将raf的指针移动到指定的偏移量
            raf.seek(offset);
            // 写入字节数组
            raf.write(bytes);
        } catch (IOException e) {
            e.printStackTrace(); // 处理异常
        }
    }

    /**
     * 从raf的offset位置开始读取len长度的数据到bytes
     *
     * @param raf    目标文件的RandomAccessFile
     * @param offset 读取的起始偏移量
     * @param len    要读取的字节长度
     * @return 读取到的字节数组
     */
    public byte[] read(RandomAccessFile raf, long offset, int len) {
        byte[] bytes = new byte[len]; // 创建一个字节数组用于存储读取的数据

        try {
            // 将raf的指针移动到指定的偏移量
            raf.seek(offset);
            // 读取数据到字节数组
            raf.readFully(bytes); // 使用readFully确保读取len长度的字节
        } catch (IOException e) {
            e.printStackTrace(); // 处理异常
        }

        return bytes; // 返回读取到的字节数组
    }

    /**
     * 将 append 的数据添加在 res 的末尾, 并返回
     * @param res
     * @param append
     * @return
     */
    public byte[] appendBytes(byte[] res, byte[] append){
        byte[] bytes = res;
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            if(res != null && append != null){
                baos.write(res);
                baos.write(append);
                bytes = baos.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

}
