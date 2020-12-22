package xmu.vis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import xmu.vis.domain.*;
import xmu.vis.mapper.*;
import xmu.vis.service.VisService;

import xmu.vis.controller.*;

import xmu.vis.utils.ResponseUtil;

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
            aNewNodeEntity_father.setNodeEntityKey(aRelationTupleEntity.getFatherNodeEntityKey());
            aNewNodeEntity_father.setNodeEntityTypeName(aRelationTupleEntity.getFatherNodeTypeName());
            aNewNodeEntity_father.setNodeEntityAttribute(aRelationTupleEntity.getFatherNodeEntityAttribute());
            nodeEntitySet.add(aNewNodeEntity_father);

            NodeEntityTable aNewNodeEntity_child = new NodeEntityTable();
            aNewNodeEntity_child.setNodeEntityKey(aRelationTupleEntity.getChildNodeEntityKey());
            aNewNodeEntity_child.setNodeEntityTypeName(aRelationTupleEntity.getChildNodeTypeName());
            aNewNodeEntity_child.setNodeEntityAttribute(aRelationTupleEntity.getChildNodeEntityAttribute());
            nodeEntitySet.add(aNewNodeEntity_child);

            // 添加关系
            if (aRelationTupleEntity.getRelationTypeName().equals("")){
                continue;
            }
            else{
                RelationTupleTable relationTupleTable = new RelationTupleTable();
                relationTupleTable.setFatherNodeKey(aRelationTupleEntity.getFatherNodeEntityKey());
                relationTupleTable.setChildNodeKey(aRelationTupleEntity.getChildNodeEntityKey());
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









}
