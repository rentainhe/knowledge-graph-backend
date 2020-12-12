package xmu.vis.service;

import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleSizeExpr;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sun.security.krb5.internal.crypto.HmacSha1Aes128CksumType;
import xmu.vis.domain.*;
import xmu.vis.mapper.*;
import xmu.vis.utils.ResponseUtil;
import xmu.vis.vo.ResNode;
import xmu.vis.vo.ResNodeType;
import xmu.vis.vo.ResRelation;


import javax.xml.soap.Node;
import javax.xml.transform.Result;
import java.lang.reflect.Field;
import java.util.*;

import static javax.swing.UIManager.put;

@Service
public class VisService {


    @Autowired
    private NodeInfoTableMapper nodeInfoTableMapper;



    //获得节点相应属性,HashMap形式返回
    public HashMap<String, String> queryNodeAttributeHashMap(NodeInfoTable node){
        //解析属性
        HashMap<String, String> nodeattributemap = new HashMap<String, String>();
        String[] array = node.getNodeinfoattribute().split(",");
        for(String attr: array){
            String[] attrname_num = attr.split(":");
            nodeattributemap.put(attrname_num[0], attrname_num[1]);
        }
        return nodeattributemap;
    }

    //根据传入节点转换成str
    public String transformJsonToStr(NodeInfoTable node){
        return "1";
    }

}
