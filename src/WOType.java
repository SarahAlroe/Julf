import org.json.JSONObject;

/**
 * Created by silasa on 3/27/17.
 */
public class WOType {
    JSONObject properties;
    public WOType(String Json) {
        properties = new JSONObject(Json);
    }
    public String getString(String key){
        return properties.getString(key);
    }
    public int getInt(String key){
        return properties.getInt(key);
    }
    public boolean getBoolean(String key){
        return properties.getBoolean(key);
    }
    public Double getDouble(String key){
        return properties.getDouble(key);
    }
    public JSONObject getJSONObject(){
        return properties;
    }

    public String getID() {
        return getString("TypeID");
    }
}
