package xyz.sky731.programming.lab6;

import xyz.sky731.programming.lab3.Bredlam;
import xyz.sky731.programming.lab5.CmdExecutor;
import xyz.sky731.programming.lab5.QueueHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class ServerMain {
    private static String fileName = System.getenv("BREDLAM_FILE");

    private static Queue<Bredlam> queue = new PriorityBlockingQueue<>
            (8, new Bredlam.BredlamNameComp());

    public static void main(String[] args) {
        loadCollectionFromDisk();

        try (DatagramSocket socket = new DatagramSocket(26425)) {
            while (true) {
                DatagramPacket packet = new DatagramPacket(new byte[16000], 16000);
                socket.receive(packet);

                new Thread(new Responder(socket, packet, new CmdExecutor(queue, fileName))).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void loadCollectionFromDisk() {
        boolean fileNotFound = false;
        if (fileName == null) {
            System.out.println("Environment variable BREDLAM_FILE not found");
            fileName = "queueFile";
        }

        if (!Files.exists(Paths.get(fileName))) {
            System.out.println("File " + fileName + " not found");
            try {
                Files.createFile(Paths.get(fileName));
            } catch (IOException e) {
                System.out.println("I can not create file. Permission denied :(");
                System.out.println("Server can not work without any way to save collection");
                System.out.println("Server stopping now..");
                System.exit(0);
            }
            System.out.println("Created file with name: " + fileName);
            fileNotFound = true;
        }
        if (!(Files.isWritable(Paths.get(fileName)) && Files.isReadable(Paths.get(fileName)))) {
            System.out.println("Oops, something went wrong");
            System.out.println("Permission denied :(");
            System.exit(0);
        }

        QueueHandler handler = new QueueHandler(queue);
        if (!fileNotFound) {
            queue = handler.loadFromFile(fileName);
        }

    }
}
