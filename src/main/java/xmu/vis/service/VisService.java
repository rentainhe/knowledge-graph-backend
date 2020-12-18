package xmu.vis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xmu.vis.controller.TableKeywords;
import xmu.vis.domain.*;
import xmu.vis.mapper.*;


import javax.validation.constraints.Max;
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

    //根据传入的hashmaps解析成str   前端   ----->  数据库(投产)
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

    //将NodeTypeTable以TableKeywords形式返回前端    数据库  -----> 前端
    public List<TableKeywords> transformNodeTypeTableintoTableKeywords(List<NodeTypeTable> allNodeTypeTable){
        List<TableKeywords> result = new ArrayList<>();
        for (NodeTypeTable aNodeTypeTable: allNodeTypeTable){
            TableKeywords aTableKeywords = new TableKeywords();
            aTableKeywords.setTableName(aNodeTypeTable.getNodetypename());//set表名

            List<HashMap<String, String>> aListNodeTypeAttribute = new ArrayList<>();
            String[] array = aNodeTypeTable.getNodetypeattribute().split(",");
            for (String attr : array) {
                HashMap<String, String> aAttribute = new HashMap<String, String>();
                aAttribute.put("value", attr);
                aListNodeTypeAttribute.add(aAttribute);
            }
            aTableKeywords.setKeyWords(aListNodeTypeAttribute);
            result.add(aTableKeywords);
        }
        return result;
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