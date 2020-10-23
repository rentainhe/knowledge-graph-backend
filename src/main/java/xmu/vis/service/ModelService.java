package xmu.vis.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import xmu.vis.utils.ParseUtil;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

@Service
public class ModelService {
    public static String HOST = "10.24.82.10";

    public static Integer PORT = 12345;

    public Object extractKnowledge(String content) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", content);
        String str = jsonObject.toJSONString();
        System.out.println(str);
        Socket socket = null;
        System.out.println("调用远程接口:host=>"+HOST+",port=>"+PORT);
        ArrayList<String> end = new ArrayList<>();
        try {
            socket = new Socket(HOST, PORT);
            // 获取输出流对象
            OutputStream os = socket.getOutputStream();
            PrintStream out = new PrintStream(os);
            // 发送内容
            out.print(str);
            // 告诉服务进程，内容发送完毕，可以开始处理
            out.print("over");
            // 获取服务进程的输入流
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
            String tmp = null;
            StringBuilder sb = new StringBuilder();
            // 读取内容
            sb.append(br.readLine());
            System.out.println(sb.toString());
            if (sb.toString().equals("500")) {
                return null;
            }
            // 解析结果
            else {
                String[] threeTriple = ParseUtil.parseKnowledge(sb.toString()).split("\n");
                System.out.println(threeTriple);
                end.add(threeTriple[0]);
                end.add(threeTriple[1]);
                end.add(threeTriple[2]);
                return end;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket!=null) {
                    socket.close();
                }} catch (IOException e) {}
            System.out.println("远程接口调用结束.");
        }
        return null;
    }
}
