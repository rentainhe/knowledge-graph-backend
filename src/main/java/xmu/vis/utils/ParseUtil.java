package xmu.vis.utils;

import com.alibaba.fastjson.JSONObject;

public class ParseUtil {
    public static String parseKnowledge(String str) {
        JSONObject jsonObject = JSONObject.parseObject(str);
        String s = jsonObject.getString("知识");
        String[] ss = s.substring(1, s.length()-1).split("[\",]");
        StringBuilder sb = new StringBuilder();
        for (String item : ss) {
            if (!item.equals("")) {
                sb.append(item).append('\n');
            }
        }
        return sb.toString();
    }
}
