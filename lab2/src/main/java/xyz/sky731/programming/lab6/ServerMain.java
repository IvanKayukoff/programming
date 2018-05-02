package xyz.sky731.programming.lab6;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerMain {
    public static void main(String[] args) {
        try (DatagramSocket ds = new DatagramSocket(26425)) {
            while (true) {
                DatagramPacket pack = new DatagramPacket(new byte[1024], 1024);
                ds.receive(pack);

                new Thread(new Responder(ds, pack)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
