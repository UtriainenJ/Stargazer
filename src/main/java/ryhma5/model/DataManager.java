package ryhma5.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager{

    private String directoryPath = "src/main/resources/json/";

    public void saveData(Map<String, Object> data, String fileName){
        String filePath = directoryPath + fileName +".json";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(filePath)){
            gson.toJson(data, writer);
            System.out.println("Data saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * TODO: rename this? it doesn't work for all types of data
     * @param fileName
     * @return
     */
    public Map<String, Object> loadDataAsObject(String fileName){
        String filePath = directoryPath + fileName +".json";
        Map<String, Object> data = new HashMap<>();
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(filePath)) {
            Type mapType = new TypeToken<Map<String, Object>>() {}.getType(); // Define the type
            data = gson.fromJson(reader, mapType);
        } catch (FileNotFoundException e) {
            System.err.println("Warning: File not found - " + filePath);
            return data;
        } catch (IOException e){
            e.printStackTrace();
        }

        return data;
    }

    public <T> List<T> loadDataAsList(String fileName, Class<T> type) {
        String filePath = directoryPath + fileName + ".json";
        List<T> list = new ArrayList<>();
        Gson gson = new Gson();

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8)) {            Type listType = new TypeToken<List<City>>() {}.getType();
            list = gson.fromJson(reader, listType);
            System.out.println("Data loaded as list from " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}