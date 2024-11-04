/*
nothing uses this file. TODO: delete it if that remains the case
 */

//package ryhma5.model;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class DataProcessor {
//    private ryhma5.model.DataManager dataManager;
//
//    public DataProcessor(){
//        this.dataManager = new DataManager();
//    }
//
//    public <T> void saveData(T dataObject, String key, String fileName) {
//        Map<String, Object> data = new HashMap<>();
//        data.put(key, dataObject);
//        dataManager.saveData(data, fileName);
//    }
//
//    public Map<String, Object> loadData(String fileName){
//        Map<String, Object> data = dataManager.loadDataAsObject(fileName);
//        if(data != null && !data.isEmpty()){
//            System.out.println("Data loaded from " + fileName + " file");
//            return data;
//        } else {
//            System.out.println("Fail to load Data from " + fileName + " file");
//            return null;
//        }
//    }
//}