package ryhma5.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataManager{

    private static final String directoryPath = "src/main/resources/json/";

    public static void saveData(Object data, String fileName){
        String filePath = directoryPath + fileName +".json";
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
                .create();

        try (FileWriter writer = new FileWriter(filePath)){
            gson.toJson(data, writer);
            System.out.println("Data saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static  <T> T loadDataAsObject(String fileName, Class<T> type){
        String filePath = directoryPath + fileName +".json";
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
                .create();;
        T data = null;

        try (FileReader reader = new FileReader(filePath)) {
            data = gson.fromJson(reader, type);
            System.out.println("Data loaded as object from " + filePath);
        } catch (FileNotFoundException e) {
            System.err.println("Warning: File not found - " + filePath);
        } catch (IOException e){
            e.printStackTrace();
        }
        return data;
    }

    public static  <T> List<T> loadDataAsList(String fileName, Class<T> type) {
        String filePath = directoryPath + fileName + ".json";
        List<T> list = new ArrayList<>();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
                .create();;

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8)) {
            Type listType = TypeToken.getParameterized(List.class, type).getType();
            list = gson.fromJson(reader, listType);
            System.out.println("Data loaded as list from " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}