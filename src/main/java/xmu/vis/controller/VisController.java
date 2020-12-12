package xmu.vis.controller;

import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleSizeExpr;
import com.sun.org.apache.xerces.internal.xs.LSInputList;
import javafx.scene.control.TextFormatter;
import net.sf.json.JSONObject;
import org.apache.catalina.util.RequestUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import xmu.vis.domain.*;
import xmu.vis.mapper.*;
import xmu.vis.service.VisService;
import xmu.vis.service.*;


import xmu.vis.utils.ResponseUtil;
import xmu.vis.vo.ResNode;
import xmu.vis.vo.ResNodeType;
import xmu.vis.vo.ResRelation;

import javax.management.relation.Relation;
import javax.validation.constraints.PastOrPresent;
import javax.websocket.server.PathParam;
import javax.xml.soap.Node;
import java.util.*;



@RestController
@CrossOrigin(origins="*")
public class VisController {

    @Autowired
    private VisService visService;

    @Autowired
    private NodeInfoTableMapper nodeInfoTableMapper;


    @GetMapping("/testgetNodeInfoByid/{nodeinfoid}")
    public Object getNodeInfoByid(@PathVariable String nodeinfoid){
        NodeInfoTable noderesult = nodeInfoTableMapper.getNodeInfoByid(nodeinfoid);
        HashMap<String, String> result = visService.queryNodeAttributeHashMap(noderesult);
        return ResponseUtil.ok(result);
    }

    @PostMapping("/addNewNodeType")
    public Object addNewNodeType(@RequestBody NodeTypeTable newNodeType){
        return ResponseUtil.ok();
    }

}
