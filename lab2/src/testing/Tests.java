package testing;

import xyz.sky731.programming.lab3.Bredlam;
import xyz.sky731.programming.lab3.Human;
import xyz.sky731.programming.lab5.FileReadWriter;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class Tests {

    private static Comparator<Bredlam> bredlamComparator = (Bredlam o1, Bredlam o2) -> o2.size() - o1.size();

    public static void main(String[] args) {
        String fileName = System.getenv("BREDLAM_FILE");
        Queue<Bredlam> queue = new PriorityQueue<>(bredlamComparator);
        addDataToQueue(queue);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        FileReadWriter readWriter = new FileReadWriter(fileName);
        try {
            System.out.println(readWriter.readFromFile());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String writeString = "Heloo world! :D\nI'm writed string!";

        try {
            readWriter.writeToFile(writeString);
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        /*while (true) {
            String command;
            try {
                command = reader.readLine();
            }
            catch (IOException ex) {
                ex.printStackTrace();
                return;
            }
            if (command == null) break;
            if (command.equals("stop")) {
                pollDataFromQueue(queue);
                System.out.println("Stop triggered");
                return;
            } else if (command.equals("info")) {
                //Информация о коллекции(тип дата количество)
            } else if (command.equals("load")) {
                //Прочитать коллекцию из файла
            } else if (command.equals("remove_last")) {

            } else if (command.equals("remove_first")) {

            } else if (command.equals("save")) {

            } else if (command.equals("remove {element}")) {

            } else if (command.equals("remove_lower {element}")) {

            } else {
                System.out.println("Command not found");
            }
        }*/

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

}
