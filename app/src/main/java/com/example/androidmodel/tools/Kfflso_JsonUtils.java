package com.example.androidmodel.tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author kfflso
 * @data 2024/9/29 15:37
 * @plus:
 */
public class Kfflso_JsonUtils {

    public static HashMap<String,String> jsonStrToHashMap(String json){
        HashMap<String,String> result = new HashMap<>();
        if(json.isEmpty()) return result;
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> it = jsonObject.keys();
            while(it.hasNext()){
                String key = it.next();
                result.put(key,jsonObject.getString(key));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
