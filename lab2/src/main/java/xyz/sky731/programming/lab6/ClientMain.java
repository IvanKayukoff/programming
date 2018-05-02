package xyz.sky731.programming.lab6;

import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ClientMain {
    private String host;
    private int port;

    private ClientMain(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void sendMessage(@NotNull String mes) {
        // Using DatagramSocket
        /*try {
            byte[] data = mes.getBytes();
            InetAddress address = InetAddress.getByName(host);
            DatagramPacket pack = new DatagramPacket(data, data.length, address, port);
            DatagramSocket ds = new DatagramSocket();
            ds.send(pack);
            ds.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        // Using DatagramChannel
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put(mes.getBytes());
            buffer.flip();
            DatagramChannel channel = DatagramChannel.open();
            SocketAddress address = new InetSocketAddress(host, port);
            channel.connect(address);
            channel.send(buffer, address);
            buffer.clear();

            // getting response from server
            System.out.println(channel.getLocalAddress());
            channel.receive(buffer);
            buffer.flip();
            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ClientMain sender = new ClientMain("localhost", 26425);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            builder.append(i);
        }
        sender.sendMessage(builder.toString());
    }
}
