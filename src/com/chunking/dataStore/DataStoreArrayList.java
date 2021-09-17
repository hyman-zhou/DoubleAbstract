package com.chunking.dataStore;

import java.util.ArrayList;

/**
 * Created by Administrator on 2019/4/12 0012.
 */
public class DataStoreArrayList {
    private ArrayList dataSet;
    {
        dataSet = new ArrayList(3000000);
    }
    public boolean add(String temp){
        dataSet.add(temp);
        return true;
    }
    public boolean clear(){
        dataSet.clear();
        return true;
    }
    public boolean contains(String temp){
        return dataSet.contains(temp);
    }
}
