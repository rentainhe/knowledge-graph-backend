package xmu.vis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import xmu.vis.domain.*;
import xmu.vis.mapper.*;
import xmu.vis.service.VisService;


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

    //通过id获得一个节点的属性信息(done)
    @GetMapping("/getNodeInfoByid/{nodeentityid}")
    public Object getNodeInfoByid(@PathVariable String nodeentityid){
        NodeEntityTable noderesult = nodeEntityTableMapper.getNodeEntityAttributeByid(nodeentityid);
        HashMap<String, String> nodeattribute = visService.queryNodeAttributeHashMap(noderesult);
        return ResponseUtil.ok(nodeattribute);
    }

    //增加新的节点类型(done)
    @PostMapping("/addNewNodeType")
    public Object addNewNodeType(@RequestBody NodeTypeTable newNodeType){
        Integer flag = nodeTypeTableMapper.addNewNodeType(newNodeType);
        if (flag == 1){
            return ResponseUtil.ok();
        }
        else{
            return ResponseUtil.fail();
        }
    }


    //更新一个节点的属性信息
    @PostMapping("/updateNodeEntityAttribute")
    public Object updateNodeEntityAttribute(@RequestBody NodeEntityTable newNodeEntity){
        HashMap<String, String> nodeAttribute = new HashMap<>();
        nodeAttribute  = visService.queryNodeAttributeHashMap(newNodeEntity);
        Integer result = nodeEntityTableMapper.updateNodeEntityAttributeById(newNodeEntity.getNodeentityid(), nodeAttribute);
        if (result == 1){
            return ResponseUtil.ok();
        }
        else{
            return ResponseUtil.fail();
        }
    }
    //增加一个新的实体节点
    @PostMapping("/addNewNodeEntity")
    public Object addNewNodeEntity(@RequestBody NodeEntityTable newNodeEntity){
        String nodeType = newNodeEntity.getNodeentitytypeid();
        HashMap<String, String> nodeAttribute_map = visService.queryNodeAttributeHashMap(newNodeEntity);

        return ResponseUtil.ok();
    }

    //test
    @GetMapping("/test/{variable}")
    public Object test(@PathVariable String variable) {
        String result = visService.getNodeAttributeDictByNodeTypeId(variable);
        return ResponseUtil.ok(result);
    }

}
