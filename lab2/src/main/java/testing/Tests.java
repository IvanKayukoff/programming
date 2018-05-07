package testing;

import xyz.sky731.programming.lab3.Bredlam;
import xyz.sky731.programming.lab3.Human;
import xyz.sky731.programming.lab5.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Tests {
    public static void main(String[] args) throws IOException {
        boolean fileNotFound = false;
        String fileName = System.getenv("BREDLAM_FILE");
        if (fileName == null) {
            System.out.println("Environment variable BREDLAM_FILE not found");
            fileName = "queueFile";
        }
        if (!Files.exists(Paths.get(fileName))) {
            System.out.println("File " + fileName + " not found");
            Files.createFile(Paths.get(fileName));
            System.out.println("Created file with filename: " + fileName);
            fileNotFound = true;
        }
        if (!(Files.isWritable(Paths.get(fileName)) && Files.isReadable(Paths.get(fileName)))) {
            System.out.println("Permission denied");
            return;
        }

        Queue<Bredlam> queue = new PriorityQueue<>();
        //here u can generate random bredlams, just uncomment line below
        //addDataToQueue(queue);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        QueueHandler handler = new QueueHandler(queue);
        if (!fileNotFound) {
            queue = handler.loadFromFile(fileName);
        }

        CmdExecutor executor = new CmdExecutor(queue, fileName);

        if (queue != null && queue.size() > 0) {
            System.out.println(executor.toJson(queue.peek()));
        }

        while (true) {
            String cmd = reader.readLine();
            if (!executor.execute(cmd)) break;
        }

    }

    private static void addDataToQueue(Queue<Bredlam> queue) {
        for (int i = 0; i < 10; i++) {
            int humanCounter = new Random().nextInt(3);
            List<Human> humans = new ArrayList<>();
            for (int j = 0; j < humanCounter; j++) {
                humans.add(new Human("Human#" + j, new Random().nextInt(1000)));
            }
            Human[] humansArr = new Human[humanCounter];
            humansArr = humans.toArray(humansArr);
            Bredlam element = new Bredlam("Bredlam#" + i, humansArr);
            queue.add(element);
        }
    }

    private static void pollDataFromQueue(Queue<Bredlam> queue) {
        while (true) {
            Bredlam element = queue.poll();
            if (element == null) break;
            System.out.println("Обработка бредлама: " + element);
        }
    }

    private static String jaxbObjectToXML(Bredlam customer) {
        String xmlString = "";
        try {
            JAXBContext context = JAXBContext.newInstance(Bredlam.class);
            Marshaller m = context.createMarshaller();

            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML

            StringWriter sw = new StringWriter();
            m.marshal(customer, sw);
            xmlString = sw.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return xmlString;
    }

}
