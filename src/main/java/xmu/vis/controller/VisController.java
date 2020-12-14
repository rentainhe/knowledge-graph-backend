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

    //通过id获得一个实体节点的属性信息(done)
    @GetMapping("/getEntityNodeInfoByid/{nodeentityid}")
    public Object getEntityNodeInfoByid(@PathVariable String nodeentityid){
        NodeEntityTable noderesult = nodeEntityTableMapper.getNodeEntityAttributeByid(nodeentityid);
        HashMap<String, String> nodeattribute = visService.queryNodeAttributeHashMap(noderesult);
        return ResponseUtil.ok(nodeattribute);
    }

    //增加节点类型
    @PostMapping("/addNewNodeType")
    public Object addNewNodeType(@RequestBody NodeTypeTable newNodeType){
        Integer flag = visService.addNewNodeType(newNodeType);
        if (flag == 1){
            return ResponseUtil.ok();
        }
        else{
            return ResponseUtil.fail();
        }
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

    // 增加一系列节点类型
    @PostMapping("/addListNewNodeType")
    public Object addListNewNodeType(@RequestBody List<TableKeywords> listofNewNodeType){
        for (TableKeywords aTableKeywords : listofNewNodeType){
            String attribute_string = visService.transformHashMapintoStr(aTableKeywords.getkeyWords());
            if (attribute_string.equals("Empty HashMap")){
                return ResponseUtil.fail();
            }
            NodeTypeTable nodeTypeTable = new NodeTypeTable();
            nodeTypeTable.setNodetypeid("testid");
            nodeTypeTable.setNodetypename(aTableKeywords.getTableName());
            nodeTypeTable.setNodetypeattribute(attribute_string);
            visService.addNewNodeType(nodeTypeTable);
        }
        return ResponseUtil.ok();
    }

    //更新一个节点的属性信息
//    @PostMapping("/updateNodeEntityAttribute")
//    public Object updateNodeEntityAttribute(@RequestBody NodeEntityTable newNodeEntity){
//        HashMap<String, String> nodeAttribute = new HashMap<>();
//        nodeAttribute  = visService.queryNodeAttributeHashMap(newNodeEntity);
//        Integer result = nodeEntityTableMapper.updateNodeEntityAttributeById(newNodeEntity.getNodeentityid(), nodeAttribute);
//        if (result == 1){
//            return ResponseUtil.ok();
//        }
//        else{
//            return ResponseUtil.fail();
//        }
//    }

    //增加一个新的实体节点
    @PostMapping("/addNewNodeEntity")
    public Object addNewNodeEntity(@RequestBody NodeEntityTable newNodeEntity){
        Integer result = visService.addNewNodeEntity(newNodeEntity);
        if( result == 1){
            return ResponseUtil.ok();
        }
        else{
            return ResponseUtil.fail();
        }
    }

    //test
    @PostMapping("/test")
    public Object test(@RequestBody List<HashMap<String,String>> input) {
        String result = visService.transformHashMapintoStr(input);
        return ResponseUtil.ok(result);
    }



}
