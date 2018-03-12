package xyz.sky731.programming.lab5;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import xyz.sky731.programming.lab3.Bredlam;
import xyz.sky731.programming.lab3.Building;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class CmdExecutor {
    private final Queue<Bredlam> queue;
    private final String filename;
    private boolean isGoing = false;
    private Date createDate;

    public CmdExecutor(Queue<Bredlam> queue, String filename) {
        this.queue = queue;
        this.filename = filename;
        if (!isGoing) {
            isGoing = true;
            Runtime.getRuntime().addShutdownHook(new Thread(()->save()));
        }
        createDate = new Date();
    }
    public boolean execute(String string) {
        String cmd, arg;
        try {
            String[] arr = string.split(" ");
            cmd = arr[0];
            StringBuilder builder = new StringBuilder("");
            for (int i = 1; i < arr.length; i++) {
                builder.append(arr[i]);
            }
            arg = builder.toString();
            while (countMatches(arg, '{') > countMatches(arg, '}')) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                try {
                    arg += reader.readLine();
                } catch (IOException ex) {
                    System.out.println("Console was closed. IOException");
                    return false;
                }
            }

        } catch (NullPointerException ex) {
            System.out.println("Console was closed");
            return false;
        }

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
            case "stop":
                return false;
            default:
                System.out.println("Unknown command");
        }
        return true;
    }

    /**
     * Printing info about collection
     */
    private void info() {
        System.out.println("Type collection: " + queue.getClass());
        if (queue.size() > 0) {
            System.out.println("Type elements in collection: " + queue.peek().getClass());
        } else {
            System.out.println("Unknown elements type");
        }
        System.out.println("Size collection: " + queue.size());
        System.out.println("Create time: " + createDate);
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
        Queue<Bredlam> newQueue = new PriorityQueue<>();
        while (queue.size() > 1) {
            newQueue.add(queue.poll());
        }
        queue.clear();
        queue.addAll(newQueue);
        System.out.println(queue.size() > 0 ? "Deleted last element " + queue.poll() :
                "Collection is already empty");
    }

    /**
     * Saving queue to file
     */
    private void save() {
        QueueHandler handler = new QueueHandler(queue);
        handler.writeToFile(filename);
        System.out.println("Saved collection to file");
    }

    /**
     * Removing first element from queue
     */
    private void removeFirst() {
        Bredlam bredlam = queue.poll();
        System.out.println(bredlam != null ? "Deleted first element "
                + bredlam : "Collection is already empty");
    }

    /**
     * Remove element by value
     * @param arg - json-formatted bredlam
     */
    private void remove(String arg) {
        Bredlam bredlam = fromJson(arg);
        if (bredlam == null) {
            System.out.println("Wrong json code");
        } else {
            System.out.println("Deleted " + bredlam);
            queue.remove(bredlam);
        }
    }

    /**
     * Remove elements less then value
     * @param arg - json-formatted bredlam
     */
    private void removeLower(String arg) {
        Bredlam bredlam = fromJson(arg);
        if (bredlam == null) {
            System.out.println("Wrong json code");
        } else {
            System.out.println("Deleted bredlams less than " + bredlam);
            queue.removeIf(p -> p.compareTo(bredlam) < 0);
        }
    }

    public Bredlam fromJson(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Building.class, new BuildingAdapter());
        Gson gson = gsonBuilder.create();
        Bredlam bredlam;
        try {
            bredlam = gson.fromJson(json, Bredlam.class);
        } catch (JsonParseException ex) {
            return null;
        }

        return bredlam;
    }

    public String toJson(Bredlam bredlam) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Building.class, new BuildingAdapter());
        Gson gson = gsonBuilder.create();
        return gson.toJson(bredlam);
    }

    private int countMatches(String string, char c) {
        int matches = 0;
        for (char elem : string.toCharArray()) {
            matches += (elem == c) ? 1 : 0;
        }
        return matches;
    }
}
