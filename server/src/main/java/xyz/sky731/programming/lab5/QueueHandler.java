package xyz.sky731.programming.lab5;

import xyz.sky731.programming.lab3.Bredlam;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class QueueHandler {

    public static void writeToFile(String filename, Queue<Bredlam> queue) {
        Bredlams arr = new Bredlams();
        arr.setBredlam(new ArrayList<>());
        arr.getBredlam().addAll(queue);
        // FIXME: delete 2 below lines when all works fine
        // JAXBUser<Bredlams> jaxbUser = new JAXBUser<>(filename);
        // jaxbUser.marshal(arr);
        XmlUser xmlUser = new XmlUser(filename);
        xmlUser.marshal(arr);
    }

    public static PriorityBlockingQueue<Bredlam> loadFromFile(String filename) {
        PriorityBlockingQueue<Bredlam> queue = new PriorityBlockingQueue<>();
        // FIXME: delete 2 below lines when all works fine
        // JAXBUser<Bredlams> jaxbUser = new JAXBUser<>(filename);
        // Bredlams bredlams = jaxbUser.unmarshal(Bredlams.class);

        XmlUser xmlUser = new XmlUser(filename);
        Bredlams bredlams = xmlUser.unmarshal();

        if (bredlams == null) {
            System.out.println("Wrong xml code");
        } else if (bredlams.getBredlam() != null) {
            queue.addAll(bredlams.getBredlam());
        }

        return queue;
    }
}
