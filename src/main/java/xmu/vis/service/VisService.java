package xmu.vis.service;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xmu.vis.controller.RelationTupleEntity;
import xmu.vis.controller.TableKeywords;
import xmu.vis.domain.*;
import xmu.vis.mapper.*;


import javax.management.relation.Relation;
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

    @Autowired
    private RelationTypeTableMapper relationTypeTableMapper;


    //根据传入的TableKeywords的hashmaps解析成str   前端----->数据库(done)
    /*
        hashmaps:[{"value":"xxx"},{"value":"xxx"},{"value":"xxx"},{"value":"xxx"},...]
     */
    public String transformTableKeywordsHashMapintoStr(List<HashMap<String, String>> attribute_hashmaps) {
        if (attribute_hashmaps.isEmpty()) {
            return "Empty HashMap";
        } else {
            StringBuilder attribute_string = new StringBuilder("");
            for (HashMap<String, String> attribute_hashmap : attribute_hashmaps) {
                attribute_string.append(attribute_hashmap.get("value"));
                attribute_string.append(",");
            }
            return attribute_string.substring(0, attribute_string.length() - 1);
        }
    }

    public String transformTableKeywordsEntityHashMapintoStr(List<HashMap<String, String>> attribute_hashmaps) {
        if (attribute_hashmaps.isEmpty()) {
            return "Empty HashMap";
        } else {
            StringBuilder attribute_string = new StringBuilder("");
            for (HashMap<String, String> attribute_hashmap : attribute_hashmaps) {
                for (Map.Entry<String, String> stringStringEntry : attribute_hashmap.entrySet()) {
                    Object key = ((Map.Entry) stringStringEntry).getKey();
                    Object value = ((Map.Entry) stringStringEntry).getValue();
                    attribute_string.append(key);
                    attribute_string.append(":");
                    attribute_string.append(value);
                    attribute_string.append(",");
                }
            }
            return attribute_string.substring(0, attribute_string.length() - 1);
        }
    }

    // 给NodeTypeTable转换成TableKeywords
    public List<TableKeywords> transformNodeTypeTableintoTableKeywords(List<NodeTypeTable> allNodeTypeTable) {
        List<TableKeywords> result = new ArrayList<>();
        for (NodeTypeTable aNodeTypeTable : allNodeTypeTable) {
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

    // 给RelationTypeTable转换成TableKeywords
    public List<TableKeywords> transformRelationTypeTableintoTableKeywords(List<RelationTypeTable> allRelationTypeTable) {
        List<TableKeywords> result = new ArrayList<>();
        for (RelationTypeTable relationTypeTable : allRelationTypeTable) {
            TableKeywords aTableKeywords = new TableKeywords();
            aTableKeywords.setTableName(relationTypeTable.getRelationTypeName());
            if (relationTypeTable.getRelationTypeAttribute().length() == 0 || relationTypeTable.getRelationTypeAttribute() == null) {
                aTableKeywords.setKeyWords(null);
            } else {
                List<HashMap<String, String>> aListRelationTypeAttribute = new ArrayList<>();
                String[] array = relationTypeTable.getRelationTypeAttribute().split(",");
                for (String attr : array) {
                    HashMap<String, String> aAttribute = new HashMap<String, String>();
                    aAttribute.put("value", attr);
                    aListRelationTypeAttribute.add(aAttribute);
                    aTableKeywords.setKeyWords(aListRelationTypeAttribute);
                }
            }
            result.add(aTableKeywords);
        }
        return result;
    }

    public List<TableKeywords> transformNodeEntityTableintoTableKeywords(List<NodeEntityTable> allNodeEntityTable) {
        List<TableKeywords> result = new ArrayList<>();
        for (NodeEntityTable aNodeEntityTable : allNodeEntityTable) {
            TableKeywords aTableKeywords = new TableKeywords();
            aTableKeywords.setTableName(aNodeEntityTable.getNodeEntityTypeName());//set表名

            List<HashMap<String, String>> aListNodeTypeAttribute = new ArrayList<>();
            String[] array = aNodeEntityTable.getNodeEntityAttribute().split(",");
            for (String attr : array) {
                HashMap<String, String> aAttribute = new HashMap<String, String>();
                String[] keyvalue = attr.split(":");
                aAttribute.put(keyvalue[0], keyvalue[1]);
                aListNodeTypeAttribute.add(aAttribute);
            }
            aTableKeywords.setKeyWords(aListNodeTypeAttribute);
            result.add(aTableKeywords);
        }
        return result;
    }

    // 给RelationTupleTable转换成TableKeywords
    public List<TableKeywords> transformRelationTupleTableintoTableKeywords(List<RelationTupleTable> allRelationTupleTable) {
        List<TableKeywords> result = new ArrayList<>();

        for (RelationTupleTable relationTupleTable : allRelationTupleTable) {
            TableKeywords tableKeywords = new TableKeywords();
            tableKeywords.setTableName(relationTupleTable.getRelationKey());
            List<HashMap<String, String>> listRelationTupleTableOthers = new ArrayList<>();

            HashMap<String, String> relationTypeName = new HashMap<String, String>();
            relationTypeName.put("relationTypeName", relationTupleTable.getRelationTypeName());
            listRelationTupleTableOthers.add(relationTypeName);
            tableKeywords.setKeyWords(listRelationTupleTableOthers);

            HashMap<String, String> fatherNodeKey = new HashMap<String, String>();
            fatherNodeKey.put("fatherNodeKey", relationTupleTable.getFatherNodeKey());
            listRelationTupleTableOthers.add(fatherNodeKey);
            tableKeywords.setKeyWords(listRelationTupleTableOthers);

            HashMap<String, String> childNodeKey = new HashMap<String, String>();
            childNodeKey.put("childNodeKey", relationTupleTable.getChildNodeKey());
            listRelationTupleTableOthers.add(childNodeKey);
            tableKeywords.setKeyWords(listRelationTupleTableOthers);

            String[] array = relationTupleTable.getRelationAttribute().split(",");
            for (String attr : array) {
                HashMap<String, String> attribute = new HashMap<String, String>();
                attribute.put("value", attr);
                listRelationTupleTableOthers.add(attribute);
                tableKeywords.setKeyWords(listRelationTupleTableOthers);
            }
            result.add(tableKeywords);
        }

        return result;
    }

    //给Tablekeys实体 转换成NodeEntity
    public NodeEntityTable fromTableKeystoNodeEntity(TableKeywords tableKeywords) {
        NodeEntityTable result = new NodeEntityTable();

        String typeName = tableKeywords.getTableName();
        List<HashMap<String, String>> attribute_hashmap = tableKeywords.getKeyWords();
        String attribute_string = transformTableKeywordsEntityHashMapintoStr(attribute_hashmap);
        String key = getKeyAttributeValueofNodeEntity(attribute_string, typeName);

        result.setNodeEntityKey(key);
        result.setNodeEntityTypeName(typeName);
        result.setNodeEntityAttribute(attribute_string);
        return result;
    }




    /* --------

    -------*/

    // 给 nodeTypeName 给出这个类型 Key
    public String getKeyofNodeType(String nodeTypeName) {
        String nodeAttribute_string = nodeTypeTableMapper.getNodeAttributebyNodeTypeName(nodeTypeName);
        String[] array = nodeAttribute_string.split(",");
        return array[0];
    }

    // 给 实体属性（字符串）实体类型，给出实体主键的值
    public String getKeyAttributeValueofNodeEntity(String nodeEntityAttribute, String nodeEntityTypeName) {
        HashMap<String, String> attributeHashMap = transformAttributeStringintoHashMap(nodeEntityAttribute);
        String key = getKeyofNodeType(nodeEntityTypeName);
        return attributeHashMap.get(key);
    }

    // 把 字符串转化为 Json 字典
    public HashMap<String, String> transformAttributeStringintoHashMap(String attributeString) {
        String[] array = attributeString.split(",");
        HashMap<String, String> result = new HashMap<>();
        for (String arrayele : array) {
            String[] keyvalue = arrayele.split(":");
            result.put(keyvalue[0], keyvalue[1]);
        }
        return result;
    }

    // 初始化返回前五个节点
    public List<NodeEntityTable> initGraph() {
        return nodeEntityTableMapper.initGraph();
    }

    // 通过父节点名字查询到所有关系
    public List<RelationTupleTable> getRelationTupleFromFatherNodeKey(String fatherNodeKey) {
        return relationTupleTableMapper.getRelationTupleFromFatherNodeKey(fatherNodeKey);
    }

    // 通过父节点名字查询到所有关系
    public List<RelationTupleTable> getRelationTupleFromChildNodeKey(String childNodeKey) {
        return relationTupleTableMapper.getRelationTupleFromChildNodeKey(childNodeKey);
    }

    // 给定节点,返回一阶关系节点
    public List<NodeEntityTable> getOneStageNodeEntitybyRootNodeEntity(NodeEntityTable rootNodeEntity) {
        HashSet<NodeEntityTable> nodeEntitySet = new HashSet<>();
        List<RelationTupleTable> aboutFatherRelationTuple = getRelationTupleFromFatherNodeKey(rootNodeEntity.getNodeEntityKey());
        for (RelationTupleTable relationTupleTable : aboutFatherRelationTuple) {
            NodeEntityTable childNode = getNodeEntityByNodeKey(relationTupleTable.getChildNodeKey());
            nodeEntitySet.add(childNode);
        }
        List<RelationTupleTable> aboutChildRelationTuple = getRelationTupleFromChildNodeKey(rootNodeEntity.getNodeEntityKey());
        for (RelationTupleTable relationTupleTable : aboutChildRelationTuple) {
            NodeEntityTable fatherNode = getNodeEntityByNodeKey(relationTupleTable.getFatherNodeKey());
            nodeEntitySet.add(fatherNode);
        }
        return new ArrayList<>(nodeEntitySet);
    }

    //根据节点ID 返回该节点所有一阶关系
    public List<RelationTupleTable> getAlloneStageRelationTuple(String nodeId) {
        List<RelationTupleTable> oneStageRelationTuple = getRelationTupleFromFatherNodeKey(nodeId);
        oneStageRelationTuple.addAll(getRelationTupleFromChildNodeKey(nodeId));
        return oneStageRelationTuple;
    }

    public List<TableKeywords> getOneStageNodeRelationTupleTable(NodeEntityTable rootNodeEntity) {
        List<RelationTupleTable> oneStageRelationTupleTable = getRelationTupleFromFatherNodeKey(rootNodeEntity.getNodeEntityKey());
        oneStageRelationTupleTable.addAll(getRelationTupleFromChildNodeKey(rootNodeEntity.getNodeEntityKey()));

        List<String> oneStageNodeId = new ArrayList<>();
        for (RelationTupleTable relationTupleTable : oneStageRelationTupleTable) {
            if (relationTupleTable.getFatherNodeKey().equals(rootNodeEntity.getNodeEntityKey())) {
                oneStageNodeId.add(relationTupleTable.getChildNodeKey());
            } else {
                oneStageNodeId.add(relationTupleTable.getFatherNodeKey());
            }
        }

        Set<RelationTupleTable> result = new HashSet<>();
        for (String nodeId : oneStageNodeId) {
            List<RelationTupleTable> relations = getAlloneStageRelationTuple(nodeId);
            result.addAll(relations);
        }
        result.removeIf(relationTupleTable -> !oneStageNodeId.contains(relationTupleTable.getFatherNodeKey()) || !oneStageNodeId.contains(relationTupleTable.getChildNodeKey()));
        List<RelationTupleTable> a = new ArrayList<>(result);
        oneStageRelationTupleTable.addAll(a);

        return transformRelationTupleTableintoTableKeywords(oneStageRelationTupleTable);

    }

    public NodeEntityTable getNodeEntityByNodeKey(String nodeEntityKey) {
        return nodeEntityTableMapper.getNodeEntityByNodeKey(nodeEntityKey);
    }

    public Integer deleteNodeType(String nodeTypeName) {
        return nodeTypeTableMapper.deleteNodeType(nodeTypeName);
    }

    public Integer addNewNodeType(NodeTypeTable newNodeType) {
        return nodeTypeTableMapper.addNewNodeType(newNodeType);
    }

    public Integer addNewNodeEntity(NodeEntityTable newNodeEntity) {
        return nodeEntityTableMapper.addNewNodeEntity(newNodeEntity);
    }

    public Integer addNewRelationType(RelationTypeTable newRelationType) {
        return relationTypeTableMapper.addNewRelationType(newRelationType);
    }

    public Integer addNewRelationTuple(RelationTupleTable relationTupleTable) {
        return relationTupleTableMapper.addNewRelationTuple(relationTupleTable);
    }
}