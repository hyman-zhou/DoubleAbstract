package com.chunking.dataStore;

import java.util.HashMap;

/**
 * Created by Administrator on 2019/4/8.
 */
public class DataStoreHashMap {
    private HashMap<String,Integer> hashMap;
    {
        hashMap=new HashMap<>();
    }
    public boolean put(String key,int value){
        hashMap.put(key,Integer.valueOf(value));
        return true;
    }
    public int get(String key){
        return hashMap.get(key).intValue();
    }
    public boolean contains(String key){
        return hashMap.containsKey(key);
    }
}
