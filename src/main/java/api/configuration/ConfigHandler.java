package api.configuration;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;

public class ConfigHandler {

    private File file;
    private HashMap<String, String> values;

    public ConfigHandler(String configName){
        values = new HashMap<>();
        checkConfigFolder();
        file = new File("config/" + configName + ".json");
        checkFileExist();
        reload();

    }

    //CREATES NEW CONFIG FOLDER
    private void checkConfigFolder(){
        if(!new File("config").exists()){
            new File("config").mkdirs();
        }
    }

    //SAVES DATA TO JSON OBJECT
    public void save(){
        JSONObject config = new JSONObject();
        for(String key : values.keySet()){
            config.put(key, values.get(key));
        }
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(new Gson().toJson(config));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //LOADS PARAMETER FROM FILE
    public void reload(){
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String json = resultStringBuilder.toString();
        if(json != null && !json.isEmpty()){
            JSONObject config = new Gson().fromJson(json, JSONObject.class);
            for(String keys : config.keySet()){
                this.set(keys, config.getString(keys));
            }
        }
    }

    //SET PARAMETER
    public void set(String parameter, String value){
        values.put(parameter, value);
    }

    //CHECKS IF VALUE CONTAINS
    public boolean contains(String parameter){
        return values.containsKey(parameter);
    }

    //RETURNS PARAMETER
    public String get(String parameter){
        return  values.get(parameter);
    }

    public Integer getInteger(String parameter){
       try{
           return Integer.parseInt(values.get(parameter));
       }catch (Exception e){
           return null;
       }
    }

    //CREATES CONFIGFILE IF NEEDED
    private void checkFileExist(){
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
