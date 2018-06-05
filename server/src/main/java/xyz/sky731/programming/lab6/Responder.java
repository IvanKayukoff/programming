package xyz.sky731.programming.lab6;

import xyz.sky731.programming.lab3.Bredlam;
import xyz.sky731.programming.lab5.CmdExecutor;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.Queue;

public class Responder implements Runnable {
    private static final int HASH_SIZE = 32;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private CmdExecutor executor;

    Responder(DatagramSocket socket, DatagramPacket packet, String filename, Queue<Bredlam> queue) {
        this.socket = socket;
        this.packet = packet;
        this.executor = new CmdExecutor(queue, filename);
    }

    public void run() {
        try {
            String data = new String(packet.getData(), 0, packet.getLength() - HASH_SIZE);
            String hash = new String(packet.getData(), packet.getLength() - HASH_SIZE, HASH_SIZE);

            ByteArrayInputStream bais = new ByteArrayInputStream
                    (Arrays.copyOfRange(packet.getData(), 0, packet.getLength() - HASH_SIZE));
            ObjectInputStream ois = null;
            Request request = null;
            try {
                ois = new ObjectInputStream(bais);
                request = (Request) ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Bredlam bredlam = request.getBredlam();
            String command = request.getCmd();
            if (bredlam != null) {
                System.out.println(bredlam);
            }
            System.out.println(command);

            String answerStr;
            boolean alright = false;
            //System.out.println(HashSum.MD5(data));
            if (hash.equals(HashSum.MD5(data))) {
                alright = executor.execute(command, bredlam);
                answerStr = "Request delivered successful\n";
                answerStr += executor.getResponse();
            } else {
                System.out.println("REQUEST IS BROKEN!");
                answerStr = "REQUEST IS BROKEN :( Try again";
            }

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
