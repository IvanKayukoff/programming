package xyz.sky731.programming.lab6;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Responder implements Runnable {
    private DatagramSocket socket;
    private DatagramPacket packet;

    Responder(DatagramSocket socket, DatagramPacket packet) {
        this.socket = socket;
        this.packet = packet;
    }

    public void run() {
        try {
            String data = new String(packet.getData(), 0, packet.getLength());
            System.out.println(data);

            String answerStr = "Message delivered";
            byte[] answer = answerStr.getBytes();
            packet = new DatagramPacket(answer, answer.length, packet.getAddress(), packet.getPort());
            socket.send(packet);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
