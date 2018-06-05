package xyz.sky731.programming.lab5;

import xyz.sky731.programming.lab3.Bredlam;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

public class CmdExecutor {
    private final Queue<Bredlam> queue;
    private final String filename;
    private static boolean isGoing = false;
    private Date createDate;
    private StringBuffer response;

    public CmdExecutor(Queue<Bredlam> queue, String filename) {
        this.queue = queue;
        this.filename = filename;
        this.response = new StringBuffer();
        if (!isGoing) {
            isGoing = true;
            Runtime.getRuntime().addShutdownHook(new Thread(() -> save()));
        }
        createDate = new Date();
    }

    public String getResponse() {
        return response.toString();
    }

    public boolean execute(String cmd, Bredlam arg) {
        switch (cmd) {
            case "info":
                info();
                break;
            case "load":
                readQueue();
                break;
            case "remove_last":
                removeLast();
                break;
            case "remove_first":
                removeFirst();
                break;
            case "save":
                save();
                break;
            case "remove":
                remove(arg);
                break;
            case "remove_lower":
                removeLower(arg);
                break;
            case "add":
                add(arg);
                break;
            case "stop":
                response.append("Stopping server..");
                return false;
            default:
                response.append("Unknown command");
        }
        return true;
    }

    /**
     * Adding bredlam to queue by value
     *
     * @param bredlam bredlam
     */
    private void add(Bredlam bredlam) {
        if (bredlam == null) {
            response.append("Wrong json code");
        } else {
            queue.add(bredlam);
            response.append("Added bredlam to queue: ");
            response.append(bredlam);
            response.append("\n");
        }
    }

    /**
     * Printing info about collection
     */
    private void info() {
        response.append("Type collection: " + queue.getClass());
        response.append("\n");
        if (queue.size() > 0) {
            response.append("Type elements in collection: " + queue.peek().getClass());
            response.append("\n");
        } else {
            response.append("Unknown elements type");
            response.append("\n");
        }
        response.append("Size collection: " + queue.size());
        response.append("\n");
        response.append("Create time: " + createDate);
        response.append("\n");
    }

    /**
     * Loading queue from file
     */
    private void readQueue() {
        QueueHandler handler = new QueueHandler(queue);
        queue.clear();
        Queue readQueue = handler.loadFromFile(filename);
        if (readQueue != queue) {
            queue.addAll(handler.loadFromFile(filename));
        }
    }

    /**
     * Removing last element from queue
     */
    private void removeLast() {
        if (queue.size() > 0) {
            response.append("Deleted last element " + queue.poll());
        } else {
            response.append("Collection is already empty");
            return;
        }
        PriorityBlockingQueue<Bredlam> bredlams = queue.stream()
                .limit(queue.size() - 1)
                .collect(Collectors.toCollection(PriorityBlockingQueue::new));
        queue.clear();
        queue.addAll(bredlams);
        response.append("\n");
    }

    /**
     * Saving queue to file
     */
    private void save() {
        QueueHandler handler = new QueueHandler(queue);
        handler.writeToFile(filename);
        response.append("Saved collection to file\n");
    }

    /**
     * Removing first element from queue
     */
    private void removeFirst() {
        Bredlam bredlam = queue.poll();
        response.append(bredlam != null ? "Deleted first element "
                + bredlam : "Collection is already empty");
        response.append("\n");
    }

    /**
     * Remove element by value
     * @param bredlam this gonna be deleted
     */
    private void remove(Bredlam bredlam) {
        if (bredlam == null) {
            response.append("Wrong json code");
        } else {
            boolean deleted = queue.remove(bredlam);

            response.append(deleted ? "Deleted " + bredlam :
                    bredlam + " doesn't exist in the collection");
        }
        response.append("\n");
    }

    /**
     * Remove elements less then value
     * @param bredlam this gonna be deleted
     */
    private void removeLower(Bredlam bredlam) {
        if (bredlam == null) {
            response.append("Wrong json code");
        } else {
            //boolean deleted = queue.removeIf(p -> p.compareTo(bredlam) < 0);
            PriorityBlockingQueue<Bredlam> bredlams = queue.stream()
                    .filter(p -> p.compareTo(bredlam) < 0)
                    .collect(Collectors.toCollection(PriorityBlockingQueue::new));
            boolean deleted = bredlams.size() < queue.size();
            queue.clear();
            queue.addAll(bredlams);
            response.append(deleted ? "Deleted bredlams less than " + bredlam :
                    "Nothing deleted");
        }
        response.append("\n");
    }

    public static int countMatches(String string, char c) {
        int matches = 0;
        for (char elem : string.toCharArray()) {
            matches += (elem == c) ? 1 : 0;
        }
        return matches;
    }
}
