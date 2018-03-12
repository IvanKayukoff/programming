package xyz.sky731.programming.lab5;

import com.google.gson.*;
import xyz.sky731.programming.lab3.Building;

import java.lang.reflect.Type;

public class BuildingAdapter implements JsonSerializer<Building>, JsonDeserializer<Building> {
    @Override
    public JsonElement serialize(Building src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
        result.add("properties", context.serialize(src, src.getClass()));
        return result;
    }

    @Override
    public Building deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");

        try {
            return context.deserialize(element, Class.forName("xyz.sky731.programming.lab3." + type));
        } catch (ClassNotFoundException ex) {
            throw new JsonParseException("Unknown element type: " + type, ex);
        }
    }
}
