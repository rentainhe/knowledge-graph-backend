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

    // 批量上传实体数据
    @PostMapping("/addListNewNodeEntity")
    public Object addListNewNodeEntity(@RequestBody List<RelationTupleEntity> listofNewNodeEntity){
        HashSet<NodeEntityTable> nodeEntitySet = new HashSet<>();
        for (RelationTupleEntity aRelationTupleEntity: listofNewNodeEntity){
            NodeEntityTable aNewNodeEntity_father = new NodeEntityTable();

            aNewNodeEntity_father.setNodeEntityTypeName(aRelationTupleEntity.getFatherNodeTypeName());
            aNewNodeEntity_father.setNodeEntityAttribute(aRelationTupleEntity.getFatherNodeEntityAttribute());
            String father_key = visService.getKeyAttributeValueofNodeEntity(aRelationTupleEntity.getFatherNodeEntityAttribute(), aRelationTupleEntity.getFatherNodeTypeName());
            aNewNodeEntity_father.setNodeEntityKey(father_key);
            nodeEntitySet.add(aNewNodeEntity_father);

            NodeEntityTable aNewNodeEntity_child = new NodeEntityTable();
            aNewNodeEntity_child.setNodeEntityTypeName(aRelationTupleEntity.getChildNodeTypeName());
            aNewNodeEntity_child.setNodeEntityAttribute(aRelationTupleEntity.getChildNodeEntityAttribute());
            String child_key = visService.getKeyAttributeValueofNodeEntity(aRelationTupleEntity.getChildNodeEntityAttribute(), aRelationTupleEntity.getChildNodeTypeName());
            aNewNodeEntity_child.setNodeEntityKey(child_key);
            nodeEntitySet.add(aNewNodeEntity_child);

            // 添加关系
            if (aRelationTupleEntity.getRelationTypeName().equals("")){
                continue;
            }
            else{
                RelationTupleTable relationTupleTable = new RelationTupleTable();
                relationTupleTable.setFatherNodeKey(visService.getKeyAttributeValueofNodeEntity(aRelationTupleEntity.getFatherNodeEntityAttribute(), aRelationTupleEntity.getFatherNodeTypeName()));
                relationTupleTable.setChildNodeKey(visService.getKeyAttributeValueofNodeEntity(aRelationTupleEntity.getChildNodeEntityAttribute(), aRelationTupleEntity.getChildNodeTypeName()));
                relationTupleTable.setRelationTypeName(aRelationTupleEntity.getRelationTypeName());
                relationTupleTable.setRelationAttribute(aRelationTupleEntity.getRelationTypeAttribute());
                visService.addNewRelationTuple(relationTupleTable);
            }
        }

        // 添加到nodeEntityTable
        for (NodeEntityTable nodeEntityTable: nodeEntitySet) {
            visService.addNewNodeEntity(nodeEntityTable);
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
        HashSet<NodeEntityTable> nodeSet = new HashSet<>();
        for (NodeEntityTable node: top_5_node){
            List<RelationTupleTable> levelOneFatherRelationTuple = visService.getRelationTupleFromFatherNodeKey(node.getNodeEntityKey());
            for(RelationTupleTable relationTupleTable: levelOneFatherRelationTuple) {
                nodeSet.add(visService.getNodeEntityByNodeKey(relationTupleTable.getChildNodeKey()));
                //根据key值查nodeEntityTable
            }
            List<RelationTupleTable> levelOneChildRelationTuple = visService.getRelationTupleFromChildNodeKey(node.getNodeEntityKey());
            for(RelationTupleTable relationTupleTable: levelOneChildRelationTuple) {
                nodeSet.add(visService.getNodeEntityByNodeKey(relationTupleTable.getFatherNodeKey()));
                //根据key值查nodeEntityTable
            }
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
