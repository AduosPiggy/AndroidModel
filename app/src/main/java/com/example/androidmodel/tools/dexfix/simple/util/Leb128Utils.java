package com.example.androidmodel.tools.dexfix.simple.util;

import java.nio.ByteBuffer;

public class Leb128Utils {

    public static int readULeb128(ByteBuffer buffer) {
        int result = 0;
        int cur;
        int count = 0;
        do {
            cur = buffer.get() & 0xff; // 读取当前字节并转换为无符号整型
            result |= (cur & 0x7f) << (count * 7); // 将低7位添加到结果中
            count++;
        } while ((cur & 0x80) == 0x80 && count < 5); // 如果最高位是1，继续读取下一个字节

        // 检查是否未正确结束 LEB128 序列
        if ((cur & 0x80) == 0x80) {
            throw new IllegalArgumentException("Invalid LEB128 sequence");
        }
        return result;
    }

    public static int readULeb128Count(ByteBuffer buffer) {
        int result = 0;
        int cur;
        int count = 0;
        do {
            cur = buffer.get() & 0xff; // 读取当前字节并转换为无符号整型
            result |= (cur & 0x7f) << (count * 7); // 将低7位添加到结果中
            count++;
        } while ((cur & 0x80) == 0x80 && count < 5); // 如果最高位是1，继续读取下一个字节

        // 检查是否未正确结束 LEB128 序列
        if ((cur & 0x80) == 0x80) {
            throw new IllegalArgumentException("Invalid LEB128 sequence");
        }
        return count;
    }


}
