package xyz.sky731.programming.lab6;

import xyz.sky731.programming.lab3.Bredlam;
import xyz.sky731.programming.lab5.CmdExecutor;
import xyz.sky731.programming.lab5.JsonParser;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.util.LinkedList;

public class ClientMain {
    private String host;
    private int port;
    private String lastInfo = null;
    private LinkedList<String> cmdQueue;

    private ClientMain(String host, int port) {
        this.host = host;
        this.port = port;
        cmdQueue = new LinkedList<>();
    }

    private void sendMessage(byte[] message) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);


            String[] arr = new String(message).split(" ");
            if (arr.length <= 0) {
                return;
            }

            String cmd = arr[0];
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < arr.length; i++) {
                builder.append(arr[i]);
            }

            String arg = builder.toString();
            Bredlam bredlam = null;
            if (arg.length() > 0) {
                bredlam = JsonParser.fromJson(arg);
                if (bredlam == null) {
                    System.out.println();
                    return;
                }
            }

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
            System.out.println("Message \"" + cmd + (arg.length() > 0 ? " " + arg : "") + "\" sent successful");

            // getting response from server
            try {
                channel.receive(buffer);
                if (!cmdQueue.isEmpty()) {
                    String[] cmds = new String[cmdQueue.size()];
                    cmds = cmdQueue.toArray(cmds);
                    cmdQueue.clear();
                    for (String s: cmds) {
                        sendMessage(s.getBytes());
                    }
                }
            } catch (PortUnreachableException e) {
                System.out.println("Server unavailable :( Try later..");
                if (cmd.equals("info")) {
                    if (lastInfo == null) {
                        System.out.println("No one successful info was execute");
                        return;
                    } else {
                        System.out.println("Here is the last successful info:");
                        System.out.println(lastInfo);
                        return;
                    }
                } else {
                    cmdQueue.add(cmd + " " + arg);
                }
            }

            buffer.flip();
            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }

            if (cmd.equals("info")) {
                buffer.flip();
                lastInfo = new String(buffer.array(), Charset.forName("UTF-8"));
            }

            System.out.println();
            buffer.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        ClientMain sender = new ClientMain("localhost", 26425);

        sender.sendMessage("info".getBytes());

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String message = null;
        while (true) {
            try {
                message = reader.readLine();
            } catch (IOException e) {
                System.out.println("Oops, console has been closed :(");
                System.exit(1);
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
        while (arg.split("[{]").length - 1 > arg.split("[}]").length - 1) {
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
