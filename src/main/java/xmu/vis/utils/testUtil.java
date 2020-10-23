package xmu.vis.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class testUtil {
    public static void main(String[] args) {
        String s = "{\"a\":123}";
        System.out.println(s);
        JSONObject jsonObject = JSON.parseObject(s);
        System.out.println(jsonObject.get("a"));
        System.out.println(jsonObject.get("b"));
    }
}
