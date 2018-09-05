package xyz.sky731.programming.lab5;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import xyz.sky731.programming.lab3.Bredlam;

import java.io.IOException;

public class JsonParser {
    public static Bredlam fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        Bredlam result;
        try {
            result = mapper.readValue(json, Bredlam.class);
        } catch (IOException e) {
            System.out.println("Wrong json, try again :(");
            return null;
        }
        return result;
    }

    public static String toJson(Bredlam bredlam) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = null;
        try {
            jsonInString = mapper.writeValueAsString(bredlam);
        } catch (IOException e) {
            System.out.println("I can't serialise this to json :(");
            e.printStackTrace();
        }
        return jsonInString;
    }
}
