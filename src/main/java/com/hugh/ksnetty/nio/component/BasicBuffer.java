package com.hugh.ksnetty.nio.component;

import java.nio.IntBuffer;

/**
 * @author hugh
 * <p>
 * Buffer数据的存、取、读写切换
 * <p>
 * Buffer中的方法实际是对底层数组的操作
 */
public class BasicBuffer {

    public static void main(String[] args) {
        IntBuffer intBuffer = IntBuffer.allocate(5);

        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(2 * i);
        }

        // 读写切换
        intBuffer.flip();

        // 可以设置 position 、limit 等
        intBuffer.position(1);
        // 此处为[)
        intBuffer.limit(intBuffer.capacity() - 1);

        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }

    }
}
