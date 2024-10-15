//DataProcessor dataProcessor = new DataProcessor();
//dataProcessor.processData();
//dataProcessor.readData();


package project.model;

import java.util.HashMap;
import java.util.Map;

public class DataProcessor {
    private DataManager dataManager;

    public DataProcessor(){
        this.dataManager = new DataManager();
    }

    public void processData() {
        UserPreferences userPreferences = new UserPreferences("30.01.2021", "30.01.2022", 60.192059, 24.945831);
        Map<String, Object> data = new HashMap<>();
        data.put("new user preference", userPreferences);
        dataManager.saveData(data, "preference");
    }

    public void readData(){
        Map<String, Object> data = dataManager.loadData("preference");

        if(data != null && !data.isEmpty()){
            System.out.println("Data loaded from preference file");
            data.forEach((key, value) -> {
                System.out.println("Key: " + key + ", Value: " + value);
            });
        }
    }
}
