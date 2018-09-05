package xyz.sky731.programming.lab5;

import xyz.sky731.programming.lab3.Bredlam;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class QueueHandler {

    public static void writeToFile(String filename, Queue<Bredlam> queue) {
        Bredlams arr = new Bredlams();
        arr.setBredlam(new ArrayList<>());
        arr.getBredlam().addAll(queue);
        JAXBUser<Bredlams> jaxbUser = new JAXBUser<>(filename);
        jaxbUser.marshal(arr);
    }

    public static PriorityBlockingQueue<Bredlam> loadFromFile(String filename) {
        PriorityBlockingQueue<Bredlam> queue = new PriorityBlockingQueue<>();

        JAXBUser<Bredlams> jaxbUser = new JAXBUser<>(filename);
        Bredlams bredlams = jaxbUser.unmarshal(Bredlams.class);

        if (bredlams == null) {
            System.out.println("Wrong xml code");
        } else if (bredlams.getBredlam() != null) {
            queue.addAll(bredlams.getBredlam());
        }

        return queue;
    }
}
