package xmu.vis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import xmu.vis.domain.*;
import xmu.vis.mapper.*;
import xmu.vis.service.VisService;

import xmu.vis.controller.*;

import xmu.vis.utils.ResponseUtil;

import javax.management.relation.Relation;
import javax.xml.soap.Node;
import java.lang.reflect.Array;
import java.util.*;



@RestController
@CrossOrigin(origins="*")
public class VisController {

    @Autowired
    private VisService visService;

    @Autowired
    private NodeEntityTableMapper nodeEntityTableMapper;

    @Autowired
    private NodeTypeTableMapper nodeTypeTableMapper;

    // 增加一系列节点类型(done)
    @PostMapping("/addListNewNodeType")
    public Object addListNewNodeType(@RequestBody List<TableKeywords> listofNewNodeType){
        for (TableKeywords aTableKeywords : listofNewNodeType){
            String attribute_string = visService.transformTableKeywordsHashMapintoStr(aTableKeywords.getKeyWords());
            if (attribute_string.equals("Empty HashMap")){
                return ResponseUtil.fail();
            }

            NodeTypeTable nodeTypeTable = new NodeTypeTable();
            nodeTypeTable.setNodeTypeName(aTableKeywords.getTableName());
            nodeTypeTable.setNodeTypeAttribute(attribute_string);
            visService.addNewNodeType(nodeTypeTable);
        }
        return ResponseUtil.ok();
    }

    // 返回所有数据库以及对应字段(done)
    @GetMapping("/getAllDataBaseAndAttribute")
    public Object getAllDataBaseAndAttribute(){
        List<TableKeywords> result = visService.transformNodeTypeTableintoTableKeywords(nodeTypeTableMapper.getAllNodeTypeNameAndAttribute());
        if (result.size() == 0){
            return ResponseUtil.fail(-1,"null database");
        }
        else {
            return ResponseUtil.ok(result);
        }
    }

    // 增加一系列关系类型
    @PostMapping("/addListNewRelationType")
    public Object addListNewRelaitonType(@RequestBody List<TableKeywords> listofNewRelaitonType){
        for (TableKeywords aTableKeywords : listofNewRelaitonType){
            String attribute_string = visService.transformTableKeywordsHashMapintoStr(aTableKeywords.getKeyWords());
            if (attribute_string.equals("Empty HashMap")){
                return ResponseUtil.fail();
            }

            RelationTypeTable relationTypeTable = new RelationTypeTable();
            relationTypeTable.setRelationTypeName(aTableKeywords.getTableName());
            relationTypeTable.setRelationTypeAttribute(attribute_string);
            visService.addNewRelationType(relationTypeTable);
        }
        return ResponseUtil.ok();
    }

    // 批量上传实体数据
    @PostMapping("/addListNewNodeEntity")
    public Object addListNewNodeEntity(@RequestBody List<RelationTupleEntity> listofNewNodeEntity){
        HashSet<TableKeywords> tableEntitySet = new HashSet<>();
        for (RelationTupleEntity aRelationTupleEntity: listofNewNodeEntity) {
            if (aRelationTupleEntity.relation.getTableName() == null || aRelationTupleEntity.relation == null){
                tableEntitySet.add(aRelationTupleEntity.childnode);
                tableEntitySet.add(aRelationTupleEntity.fathernode);
            }
            else {
                tableEntitySet.add(aRelationTupleEntity.childnode);
                tableEntitySet.add(aRelationTupleEntity.fathernode);
                //上传三元组
                RelationTupleTable relationTupleTable = new RelationTupleTable();
                relationTupleTable.setRelationTypeName(aRelationTupleEntity.relation.getTableName());
                relationTupleTable.setRelationAttribute(visService.transformTableKeywordsEntityHashMapintoStr(aRelationTupleEntity.getRelation().getKeyWords()));
                String fatherNodeKey = visService.getKeyAttributeValueofNodeEntity(visService.transformTableKeywordsEntityHashMapintoStr(aRelationTupleEntity.fathernode.getKeyWords()), aRelationTupleEntity.fathernode.tableName);
                relationTupleTable.setFatherNodeKey(fatherNodeKey);
                String childNodeKey = visService.getKeyAttributeValueofNodeEntity(visService.transformTableKeywordsEntityHashMapintoStr(aRelationTupleEntity.childnode.getKeyWords()), aRelationTupleEntity.childnode.tableName);
                relationTupleTable.setChildNodeKey(childNodeKey);
                visService.addNewRelationTuple(relationTupleTable);
            }
        }

        // 上传节点
        for (TableKeywords aTableKeywordsNode: tableEntitySet){
            NodeEntityTable aNodeEntityTable = new NodeEntityTable();
            aNodeEntityTable.setNodeEntityTypeName(aTableKeywordsNode.getTableName());
            String nodeAttributeString = visService.transformTableKeywordsEntityHashMapintoStr(aTableKeywordsNode.getKeyWords());
            aNodeEntityTable.setNodeEntityAttribute(nodeAttributeString);
            String nodeKey = visService.getKeyAttributeValueofNodeEntity(nodeAttributeString, aTableKeywordsNode.getTableName());
            aNodeEntityTable.setNodeEntityKey(nodeKey);
            visService.addNewNodeEntity(aNodeEntityTable);
        }

        return ResponseUtil.ok();
    }


    // 删除节点类型
    @GetMapping("/deleteNodeType/{nodeTypeName}")
    public Object deleteNodeType(@PathVariable String nodeTypeName){
        Integer result = visService.deleteNodeType(nodeTypeName);
        if (result==1){
            return ResponseUtil.ok();
        }
        else{
            return ResponseUtil.fail();
        }
    }

    // 根据父节点名称找到其所有关系
//    @GetMapping("/getRelationTupleFromFatherNodeKey/{fatherNodeKey}")
//    public Object getRelationTupleFromFatherNodeKey(@PathVariable String fatherNodeKey){
//        List<RelationTupleTable> result = visService.getRelationTupleFromFatherNodeKey(fatherNodeKey);
//        return ResponseUtil.ok(result);
//    }
//
//    @GetMapping("/getRelationTupleFromChildNodeKey/{childNodeKey}")
//    public Object getRelationTupleFromChildNodeKey(@PathVariable String childNodeKey){
//        List<RelationTupleTable> result = visService.getRelationTupleFromChildNodeKey(childNodeKey);
//        return ResponseUtil.ok(result);
//    }
    // 返回init节点
    @GetMapping("initGraphNodesInfo")
    public Object initGraphNodesInfo(){
        List<NodeEntityTable> top_5_node = visService.initGraph(); // 获取前五个node节点
        HashSet<TableKeywords> nodeSet = new HashSet<>();
        for (NodeEntityTable node: top_5_node){
            List<RelationTupleTable> levelOneFatherRelationTuple = visService.getRelationTupleFromFatherNodeKey(node.getNodeEntityKey());
            List<NodeEntityTable> childNodeEntityList = new ArrayList<>();
            for(RelationTupleTable relationTupleTable: levelOneFatherRelationTuple) {
                NodeEntityTable childNode = visService.getNodeEntityByNodeKey(relationTupleTable.getChildNodeKey());
                childNodeEntityList.add(childNode);
            }
            List<TableKeywords> result1 = visService.transformNodeEntityTableintoTableKeywords(childNodeEntityList);
            nodeSet.addAll(result1);

            List<RelationTupleTable> levelOneChildRelationTuple = visService.getRelationTupleFromChildNodeKey(node.getNodeEntityKey());
            List<NodeEntityTable> fatherNodeEntityList = new ArrayList<>();
            for(RelationTupleTable relationTupleTable: levelOneFatherRelationTuple) {
                NodeEntityTable fatherNode = visService.getNodeEntityByNodeKey(relationTupleTable.getFatherNodeKey());
                fatherNodeEntityList.add(fatherNode);
            }
            List<TableKeywords> result2 = visService.transformNodeEntityTableintoTableKeywords(fatherNodeEntityList);
            nodeSet.addAll(result2);
        }
        return ResponseUtil.ok(nodeSet);
    }

    // 返回init 关系三元组
    @GetMapping("initGraphRelationTuple")
    public Object initGraphRelationTuple(){
        List<NodeEntityTable> top_5_node = visService.initGraph(); // 获取前五个node节点
        List<RelationTupleTable> RelaitonTupleResult = new ArrayList<>();
        for (NodeEntityTable node: top_5_node){
            RelaitonTupleResult.addAll(visService.getRelationTupleFromFatherNodeKey(node.getNodeEntityKey()));
            RelaitonTupleResult.addAll(visService.getRelationTupleFromChildNodeKey(node.getNodeEntityKey()));
        }
        HashSet<RelationTupleTable> result = new HashSet<>(RelaitonTupleResult);
        return ResponseUtil.ok(result);
    }






}
