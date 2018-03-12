package xyz.sky731.programming.lab5;

import org.xml.sax.SAXParseException;
import xyz.sky731.programming.lab3.Bredlam;

import java.util.*;

public class QueueHandler {
    private final Queue<Bredlam> queue;

    public QueueHandler(Queue<Bredlam> queue) {
        this.queue = queue;
    }

    public void writeToFile(String filename) {
        Bredlams arr = new Bredlams();
        arr.setList(new ArrayList<>());
        List<Bredlam> list = arr.getList();
        list.addAll(queue);
        JAXBUser<Bredlams> jaxbUser = new JAXBUser<>(filename);
        jaxbUser.marshal(arr);
    }

    public Queue<Bredlam> loadFromFile(String filename) {
        JAXBUser<Bredlams> jaxbUser = new JAXBUser<>(filename);
        Bredlams bredlams = jaxbUser.unmarshal(Bredlams.class);

        queue.clear();
        if (bredlams == null) {
            System.out.println("Wrong xml code");
        } else if (bredlams.getList() != null) {
            queue.addAll(bredlams.getList());
        }

        return queue;
    }
}
