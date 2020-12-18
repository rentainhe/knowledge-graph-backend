package xmu.vis.controller;

import java.util.HashMap;
import java.util.List;

public class TableKeywords {
    public String tableName;
    public List<HashMap<String, String>> keyWords;

    public String getTableName(){
        return this.tableName;
    }

    public List<HashMap<String, String>> getkeyWords(){
        return this.keyWords;
    }

    public void setTableName(String tableName1){
        this.tableName = tableName1;
    }

    public void setKeyWords(List<HashMap<String, String>> keyWords1){
        this.keyWords = keyWords1;
    }
}
