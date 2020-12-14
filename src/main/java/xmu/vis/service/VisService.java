package xmu.vis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xmu.vis.domain.*;
import xmu.vis.mapper.*;


import java.util.*;

@Service
public class VisService {
    @Autowired
    private NodeEntityTableMapper nodeEntityTableMapper;

    @Autowired
    private NodeTypeTableMapper nodeTypeTableMapper;

    //获得节点相应属性,HashMap形式返回
    public HashMap<String, String> queryNodeAttributeHashMap(NodeEntityTable node) {
        //解析属性
        HashMap<String, String> nodeattributemap = new HashMap<String, String>();
        String[] array = node.getNodeentityattribute().split(",");
        for (String attr : array) {
            String[] attrname_num = attr.split(":");
            nodeattributemap.put(attrname_num[0], attrname_num[1]);
        }
        return nodeattributemap;
    }

    //根据节点类型,获得属性词典
    public String getNodeAttributeDictByNodeTypeId(String nodeTypeId){
        String attribute_str = nodeTypeTableMapper.getNodeAttributeDictByNodeTypeId(nodeTypeId);
//        HashMap<String, String> attribute_hashmap = new HashMap<>();
        return attribute_str;
    }

    //根据传入的hashmaps解析成str
    /*
        hashmaps:[{"value":"xxx"},{"value":"xxx"},{"value":"xxx"},{"value":"xxx"},...]
     */
    public String transformHashMapintoStr(List<HashMap<String, String>> attribute_hashmaps){
        if(attribute_hashmaps.isEmpty()){ return "Empty HashMap"; }
        else {
                StringBuilder attribute_string = new StringBuilder("");
                for (HashMap<String, String> attribute_hashmap: attribute_hashmaps) {
                    attribute_string.append(attribute_hashmap.get("value"));
                    attribute_string.append(",");
                }
            return attribute_string.substring(0, attribute_string.length()-1);
            }
    }


    public Integer deleteNodeType(String nodeTypeName){
        return nodeTypeTableMapper.deleteNodeType(nodeTypeName);
    }

    public Integer addNewNodeType(NodeTypeTable newNodeType){
        return nodeTypeTableMapper.addNewNodeType(newNodeType);
    }

    public Integer addNewNodeEntity(NodeEntityTable newNodeEntity){
        return nodeEntityTableMapper.addNewNodeEntity(newNodeEntity);
    }
}