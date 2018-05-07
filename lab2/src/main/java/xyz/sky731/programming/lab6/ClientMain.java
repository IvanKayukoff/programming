package xyz.sky731.programming.lab6;

import com.sun.istack.internal.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.PriorityQueue;
import java.util.Queue;

public class ClientMain {
    private String host;
    private int port;

    private ClientMain(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void sendMessage(@NotNull byte[] message) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(16000);
            buffer.put(message);

            String hash = HashSum.MD5(new String(message));
            System.out.println(hash);

            buffer.put(hash.getBytes());
            buffer.flip();

            DatagramChannel channel = DatagramChannel.open();
            SocketAddress address = new InetSocketAddress(host, port);
            channel.connect(address);
            channel.send(buffer, address);
            buffer.clear();
            System.out.println("Message sent successful");

            // getting response from server
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
        /*StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            builder.append(i);
        }*/

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String message = null;
        try {
            message = reader.readLine();
        } catch (IOException e) {
            System.out.println("Oops, console has been closed :(");
            System.exit(0);
        }
        sender.sendMessage(message.getBytes());
    }
}
