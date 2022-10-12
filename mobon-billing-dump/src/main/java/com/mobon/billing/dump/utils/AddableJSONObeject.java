package com.mobon.billing.dump.utils;

import net.sf.json.JSONObject;

public abstract class AddableJSONObeject {

    private AddableJSONObeject() {
        throw new AssertionError();
    }

    public static String addInt(String key, int val, String jsonString){

        JSONObject json = toJSONObject(jsonString);

        if(json.containsKey(key)) {
            json.put(key, json.getInt(key) + val);
        } else {
            json.put(key, val);
        }

        return json.toString();
    }

    private static void addInt(String key, int val, JSONObject json){

        if(json.containsKey(key)) {
            json.put(key, json.getInt(key) + val);
        } else {
            json.put(key, val);
        }

    }

    private static String addAllInt(JSONObject json1 , JSONObject json2) {

        for (Object key : json1.keySet()) {
            addInt((String) key, json1.getInt((String) key), json2);
        }

        return json2.toString();
    }

    public static String addAllInt(String jsonString1,String jsonString2 ) {
        JSONObject addJson1 = toJSONObject(jsonString1);
        JSONObject addJson2 = toJSONObject(jsonString2);
        return addAllInt(addJson1, addJson2);
    }

    public static String addFloat(String key, float val, String jsonString){
        JSONObject json = toJSONObject(jsonString);

        if(json.containsKey(key)) {
            json.put(key, (float)(json.getDouble(key) + val));
        } else {
            json.put(key, val);
        }

        return json.toString();

    }

    private static void addFloat(String key, float val, JSONObject json){

        if(json.containsKey(key)) {
            json.put(key, (float)(json.getDouble(key) + val));
        } else {
            json.put(key, val);
        }
    }

    private static String addAllFloat(JSONObject json1, JSONObject json2) {

        for (Object key : json1.keySet()) {
            addFloat((String)key, (float)json1.getDouble((String)key), json2);
        }
        return json2.toString();
    }

    public static String addAllFloat(String jsonString1,String jsonString2) {
        JSONObject addJson1 = toJSONObject(jsonString1);
        JSONObject addJson2 = toJSONObject(jsonString2);
        return addAllFloat(addJson1,addJson2);
    }

    private static JSONObject toJSONObject(String strJson) {
        if(strJson == null)
            return null;

        JSONObject json = JSONObject.fromObject(strJson);
        return json;
    }

}
