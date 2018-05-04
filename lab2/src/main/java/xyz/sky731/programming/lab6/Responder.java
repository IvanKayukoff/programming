package xyz.sky731.programming.lab6;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

public class Responder implements Runnable {
    private static final int HASH_SIZE = 32;
    private DatagramSocket socket;
    private DatagramPacket packet;

    Responder(DatagramSocket socket, DatagramPacket packet) {
        this.socket = socket;
        this.packet = packet;
    }

    public void run() {
        try {
            String data = new String(packet.getData(), 0, packet.getLength() - HASH_SIZE);
            System.out.println(data);
            String hash = new String(packet.getData(), packet.getLength() - HASH_SIZE, HASH_SIZE);
            System.out.println(hash);

            String answerStr;
            System.out.println(HashSum.MD5(data));
            if (hash.equals(HashSum.MD5(data))) {
                answerStr = "Request delivered successful";
            } else {
                answerStr = "REQUEST IS BROKEN!";
            }
            System.out.println(answerStr);

            byte[] answer = answerStr.getBytes();
            packet = new DatagramPacket(answer, answer.length, packet.getAddress(), packet.getPort());
            socket.send(packet);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void divideBuffer(ByteBuffer src, ByteBuffer data, ByteBuffer hash, int packetLength) {
        int positionSrc = src.position();
        src.flip();
        data.clear();
        hash.clear();
        data.put(src.array(), 0, packetLength - HASH_SIZE);
        data.flip();
        src.flip();
        hash.put(src.array(), packetLength - HASH_SIZE, HASH_SIZE);
        hash.flip();

        src.position(positionSrc);
    }
}
