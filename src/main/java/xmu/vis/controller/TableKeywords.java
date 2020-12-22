package xmu.vis.controller;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class TableKeywords {
    public String tableName;
    public List<HashMap<String, String>> keyWords;

}
