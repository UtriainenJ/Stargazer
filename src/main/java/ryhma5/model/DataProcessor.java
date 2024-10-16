//UserPreferences userPreferences = new UserPreferences("30.01.2021", "30.01.2022", 60.192059, 24.945831);
// String dataKey = "new user preference";
// String fileName = "userPreferences";
// DataProcessor newData = new DataProcessor();
// newData.saveData(userPreferences, dataKey, fileName);

package ryhma5.model;

import ryhma5.model.DataManager;
import ryhma5.model.UserPreferences;

import java.util.HashMap;
import java.util.Map;

public class DataProcessor {
    private ryhma5.model.DataManager dataManager;

    public DataProcessor(){
        this.dataManager = new DataManager();
    }

    public <T> void saveData(T dataObject, String key, String fileName) {
        Map<String, Object> data = new HashMap<>();
        data.put(key, dataObject);
        dataManager.saveData(data, fileName);
    }

    public Map<String, Object> loadData(String fileName){
        Map<String, Object> data = dataManager.loadData(fileName);
        if(data != null && !data.isEmpty()){
            System.out.println("Data loaded from " + fileName + " file");
            return data;
        } else {
            System.out.println("Fail to load Data from " + fileName + " file");
            return null;
        }
    }
}