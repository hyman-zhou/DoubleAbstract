package com.chunking.dataStore;

import org.json.JSONObject;

/**
 * Administrator on 2018.11.28 15:13.
 */
public class DataStoreJson {
    //存储区块下标到区块完整数据的对应关系
    private JSONObject index2src;
    {
        index2src = new JSONObject();
    }
    public boolean setIndex2Src(JSONObject temp){
        this.index2src=temp;
        return true;
    }
    public boolean addIndex2Src(String index,String data){
        this.index2src.put(index,data);
        return true;
    }
    public JSONObject getIndex2Src(){
        return this.index2src;
    }
}
