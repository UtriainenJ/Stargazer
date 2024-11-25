package ryhma5.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import ryhma5.model.dateTimeUtils.OffsetDateTimeAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.util.List;

public class DataManager{

    private static final String directoryPath = "src/main/resources/json/";

    /**
     * Saves given data to named json file, if named file doesn't exist then create file with that name
     *
     * @param data object that is being saved.
     * @param fileName the name of file that data is going to be saved.
     */
    public static void saveData(Object data, String fileName){
        String filePath = directoryPath + fileName +".json";
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
                .create();

        try (FileWriter writer = new FileWriter(filePath)){
            gson.toJson(data, writer);
            System.out.println("Data saved to " + filePath);
        } catch (FileNotFoundException e) {
            System.err.println("Warning: File not found - " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * load data form named json file as given object type and returns this data
     *
     * @param fileName the name of file that data is going to be load from.
     * @param type object type of data that is being loaded
     * @return
     * @param <T>
     */
    public static  <T> T loadDataAsObject(String fileName, Type type){
        String filePath = directoryPath + fileName +".json";
        T data = null;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
                .create();

        try (FileReader reader = new FileReader(filePath)) {
            data = gson.fromJson(reader, type);
            System.out.println("Data loaded from " + filePath);
        } catch (FileNotFoundException e) {
            System.err.println("Warning: File not found - " + filePath);
        } catch (IOException e){
            e.printStackTrace();
        }
        return data;
    }

    /**
     * load data form named json file as given object type list and returns this data list
     *
     * @param fileName the name of file that data is going to be load from.
     * @param type object type of data that is being loaded
     * @return
     * @param <T>
     */
    public static  <T> List<T> loadDataAsList(String fileName, Class<T> type) {
        Type listType = TypeToken.getParameterized(List.class, type).getType();
        return loadDataAsObject(fileName, listType);
    }
}