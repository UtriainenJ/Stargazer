// add to pom.xml
//<dependency>
//        <groupId>com.google.code.gson</groupId>
//        <artifactId>gson</artifactId>
//        <version>2.8.9</version>
//</dependency>

//add to module-info.json
//requires com.google.gson;
//opens project.model to com.google.gson;

//public void loadData(String filePath) {
    // load
//}


package project.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class DataManager{
    private String directoryPath = "src/main/resources/savedUserPreferencesData/";

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

    public Map<String, Object> loadData(String fileName){
        String filePath = directoryPath + fileName +".json";
        Map<String, Object> data = new HashMap<>();
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(filePath)) {
            Type mapType = new TypeToken<Map<String, Object>>() {}.getType(); // Define the type
            data = gson.fromJson(reader, mapType);
        } catch (IOException e){
            e.printStackTrace();
        }

        return data;
    }
}
