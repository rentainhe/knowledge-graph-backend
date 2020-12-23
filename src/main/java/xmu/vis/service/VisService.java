package xmu.vis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xmu.vis.controller.RelationTupleEntity;
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

    @Autowired
    private RelationTupleTableMapper relationTupleTableMapper;
    //根据传入的TableKeywords的hashmaps解析成str   前端----->数据库(done)
    /*
        hashmaps:[{"value":"xxx"},{"value":"xxx"},{"value":"xxx"},{"value":"xxx"},...]
     */
    public String transformTableKeywordsHashMapintoStr(List<HashMap<String, String>> attribute_hashmaps){
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

    //将NodeTypeTable以TableKeywords形式返回前端    数据库  -----> 前端(done)
    public List<TableKeywords> transformNodeTypeTableintoTableKeywords(List<NodeTypeTable> allNodeTypeTable){
        List<TableKeywords> result = new ArrayList<>();
        for (NodeTypeTable aNodeTypeTable: allNodeTypeTable){
            TableKeywords aTableKeywords = new TableKeywords();
            aTableKeywords.setTableName(aNodeTypeTable.getNodeTypeName());//set表名

            List<HashMap<String, String>> aListNodeTypeAttribute = new ArrayList<>();
            String[] array = aNodeTypeTable.getNodeTypeAttribute().split(",");
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

    public String getKeyofNodeType(String nodeTypeName){
        String nodeAttribute_string = nodeTypeTableMapper.getNodeAttributebyNodeTypeName(nodeTypeName);
        String[] array = nodeAttribute_string.split(",");
        return array[0];
    }
    public String getKeyAttributeValueofNodeEntity(String nodeEntityAttribute, String nodeEntityTypeName){
        HashMap<String, String> attributeHashMap = transformAttributeStringintoHashMap(nodeEntityAttribute);
        String key = getKeyofNodeType(nodeEntityTypeName);
        return attributeHashMap.get(key);
    }

    public HashMap<String, String> transformAttributeStringintoHashMap(String attributeString){
        String[] array = attributeString.split(",");
        HashMap<String, String> result = new HashMap<>();
        for (String arrayele: array){
            String[] keyvalue = arrayele.split(":");
            result.put(keyvalue[0], keyvalue[1]);
        }
        return result;
    }
    //
    public Integer deleteNodeType(String nodeTypeName){
        return nodeTypeTableMapper.deleteNodeType(nodeTypeName);
    }

    public Integer addNewNodeType(NodeTypeTable newNodeType){
        return nodeTypeTableMapper.addNewNodeType(newNodeType);
    }

    public Integer addNewNodeEntity(NodeEntityTable newNodeEntity){ return nodeEntityTableMapper.addNewNodeEntity(newNodeEntity); }

    public Integer addNewRelationTuple(RelationTupleTable relationTupleTable){ return relationTupleTableMapper.addNewRelationTuple(relationTupleTable);}
}