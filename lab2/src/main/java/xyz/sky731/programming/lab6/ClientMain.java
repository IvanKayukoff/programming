package xyz.sky731.programming.lab6;

import com.sun.istack.internal.NotNull;
import xyz.sky731.programming.lab3.Bredlam;
import xyz.sky731.programming.lab5.CmdExecutor;

import java.io.*;
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

    private void sendMessage(@NotNull byte[] message) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            String[] arr = new String(message).split(" ");
            String cmd = arr[0];
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < arr.length; i++) {
                builder.append(arr[i]);
            }
            String arg = builder.toString();
            Bredlam bredlam = CmdExecutor.fromJson(arg);

            oos.writeObject(new Request(cmd, bredlam));
            oos.flush();
            byte[] array = baos.toByteArray();

            ByteBuffer buffer = ByteBuffer.allocate(16000);
            buffer.put(array);

            String hash = HashSum.MD5(new String(array));

            buffer.put(hash.getBytes());
            buffer.flip();

            DatagramChannel channel = DatagramChannel.open();
            SocketAddress address = new InetSocketAddress(host, port);
            channel.connect(address);
            channel.send(buffer, address);
            buffer.clear();
            System.out.println("Message sent successful");

            // getting response from server
            try {
                channel.receive(buffer);
            } catch (PortUnreachableException e) {
                System.out.println("Server unavailable :( Try later..");
            }
            buffer.flip();
            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }
            System.out.println();
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        
        ClientMain sender = new ClientMain("localhost", 26425);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String message = null;
        while (true) {
            try {
                message = reader.readLine();
            } catch (IOException e) {
                System.out.println("Oops, console has been closed :(");
                System.exit(0);
            }
            StringBuffer messageBuf = new StringBuffer(message);
            checkBrackets(messageBuf);
            sender.sendMessage(messageBuf.toString().getBytes());
        }
    }

    private static void checkBrackets(StringBuffer buffer) {
        String[] arr = buffer.toString().split(" ");
        String cmd = arr[0];
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < arr.length; i++) {
            builder.append(arr[i]);
        }
        String arg = builder.toString();
        while (CmdExecutor.countMatches(arg, '{') > CmdExecutor.countMatches(arg, '}')) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                arg += reader.readLine();
            } catch (IOException ex) {
                System.out.println("Oops, console is dead :(");
                System.exit(1);
            }
        }
        buffer.delete(0, buffer.length());
        buffer.append(cmd + " " + arg);
    }
}
