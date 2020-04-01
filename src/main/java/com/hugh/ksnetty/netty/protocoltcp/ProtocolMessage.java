package com.hugh.ksnetty.netty.protocoltcp;

/**
 *
 *
 *
 */
public class ProtocolMessage {

    private int length;
    private byte[] content;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public ProtocolMessage(int length, byte[] content) {
        this.length = length;
        this.content = content;
    }
}
