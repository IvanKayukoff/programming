package xyz.sky731.programming.lab6;

import xyz.sky731.programming.lab5.CmdExecutor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Responder implements Runnable {
    private static final int HASH_SIZE = 32;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private CmdExecutor executor;

    Responder(DatagramSocket socket, DatagramPacket packet, CmdExecutor executor) {
        this.socket = socket;
        this.packet = packet;
        this.executor = executor;
    }

    public void run() {
        try {
            String data = new String(packet.getData(), 0, packet.getLength() - HASH_SIZE);
            System.out.println(data);
            String hash = new String(packet.getData(), packet.getLength() - HASH_SIZE, HASH_SIZE);
            System.out.println(hash);

            boolean alright = executor.execute(data); //FIXME CHECK THIS

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
            if (!alright) {
                System.out.println("Stopping server..");
                System.exit(0);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
