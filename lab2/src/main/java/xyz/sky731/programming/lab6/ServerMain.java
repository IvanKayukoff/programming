package xyz.sky731.programming.lab6;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerMain {
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(26425)) {
            while (true) {
                DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                socket.receive(packet);

                new Thread(new Responder(socket, packet)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
