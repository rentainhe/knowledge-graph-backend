package xmu.vis.controller;

import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleSizeExpr;
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
import xmu.vis.controller.VO.*;


import xmu.vis.utils.ResponseUtil;
import xmu.vis.vo.ResNode;
import xmu.vis.vo.ResNodeType;
import xmu.vis.vo.ResRelation;

import javax.management.relation.Relation;
import javax.validation.constraints.PastOrPresent;
import javax.xml.soap.Node;
import java.util.*;



@RestController
@CrossOrigin(origins="*")
public class VisController {

    @Autowired
    private AttributeMapper attributeMapper;

    @Autowired
    private VisService visService;

    @Autowired
    private EquipmentTypeMapper equipmentTypeMapper;

    @Autowired
    private EquipmentTreeMapper equipmentTreeMapper;

    @Autowired
    private UnitSequenceMapper unitSequenceMapper;

    @Autowired
    private CharacterDataMapper characterDataMapper;

    @Autowired
    private PersonnelInformationMapper personnelInformationMapper;

    @Autowired
    private RelationTupleMapper relationTupleMapper;

    @Autowired
    private RelationTypeMapper relationTypeMapper;

    @Autowired
    private NodeInfoMapper nodeInfoMapper;

    @Autowired
    private EquipmentAllocationMapper equipmentAllocationMapper;


    /*
        更新待审核关系
     */
//    @CrossOrigin(origins="http://10.24.82.10:8010", maxAge = 3600, allowCredentials = "true")
    @PostMapping("/updataDatabaseByUncheckedRelation")
    public Object updataDatabaseByUncheckedRelation(@RequestBody List<RelationCheck> relationChecks){
        if(relationChecks.size()==0){
            return ResponseUtil.fail(-1,"the updata List can't be null");
        }
        else{
            for(int i=0; i<relationChecks.size(); i++){
                Integer result = visService.updataDatabaseByUncheckedRelation(relationChecks.get(i)); // 添加关系三元组
                if(result==0){
                    return ResponseUtil.fail(-1,"Fail to add RelationTuple");
                }
                visService.deleteUncheckedRelation(relationChecks.get(i)); // 添加成功后在待审核表中删除
            }
            return ResponseUtil.ok();
        }
    }

    /*
        根据id更新待审核节点
     */
//    @CrossOrigin(origins="http://10.24.82.10:8010", maxAge = 3600, allowCredentials = "true")
    @PostMapping("/updataUncheckedRelationById")
    public Object updataUncheckedRelationById(@RequestBody List<RelationCheck> relationChecks){
        if(relationChecks.size()==0){
            return ResponseUtil.fail(-1,"the updata List can't be null");
        }
        else{
            for(int i=0; i<relationChecks.size(); i++){
                visService.updataUncheckedRelationById(relationChecks.get(i));
            }
            return ResponseUtil.ok();
        }
    }

    /*
        根据id获取待审核关系
     */
//    @CrossOrigin(origins="http://10.24.82.10:8010", maxAge = 3600, allowCredentials = "true")
    @GetMapping("/getUncheckedRelationById/{unCheckedId}")
    public Object getUncheckedRelationById(@PathVariable String unCheckedId){
        RelationCheck relationCheck = visService.getUncheckedRelationById(unCheckedId);
        return ResponseUtil.ok(relationCheck);
    }

    /*
        删除待审核节点
     */
//    @CrossOrigin(origins="http://localhost:8010", maxAge = 3600, allowCredentials = "true")
    @PostMapping("/deleteUncheckedRelation")
    public Object deleteUncheckedRelation(@RequestBody List<RelationCheck> relationChecks){
        if (relationChecks.isEmpty()) {
            return ResponseUtil.fail(-1,"the deleted data can't be empty");
        }
        else{
            for (int i=0; i< relationChecks.size(); i++){
                visService.deleteUncheckedRelation(relationChecks.get(i)); // java里list用.get()方法获取对应下标元
            }
            return ResponseUtil.ok();
        }
    }

    /*
        将Excel中上传的所有的关系插入待审核关系表
     */
//    @CrossOrigin(origins="http://localhost:8010", maxAge = 3600, allowCredentials = "true")
    @PostMapping("/insertUncheckedRelation")
    public Object insertUncheckedRelation(@RequestBody List<RelationCheck> relationChecks){
        if (relationChecks.isEmpty()) {
            return ResponseUtil.fail(-1,"the upload Excel data can't be empty");
        }
        else{
            for (int i=0; i< relationChecks.size(); i++){
                visService.insertUncheckedRelation(relationChecks.get(i)); // java里list用.get()方法获取对应下标元
            }
            return ResponseUtil.ok();
        }
    }

    /*
        获取所有待审核的关系
     */
//    @CrossOrigin
    @GetMapping("/getAllUncheckedRelation")
    public Object getAllUncheckedRelation(){
        List<RelationCheck> allUncheckedRelation = visService.getAllUncheckedRelation();
        if(allUncheckedRelation.size()==0){
            return ResponseUtil.fail();
        }
        return ResponseUtil.ok(allUncheckedRelation);
    }


    // 查询所有节点信息
    @GetMapping("/queryAllNode")
    public Object queryAllNode(){
        List<NodeInfo> allnode = visService.getAllNodeInfo();
        if( allnode.size()==0 ){
            return ResponseUtil.fail();
        }
        return ResponseUtil.ok(allnode);
    }
    // 查询所有关系三元组
    @GetMapping("/queryAllRelation")
    public Object queryAllRelation(){
        List<RelationTuple> allrelation = visService.getAllRelationTuple();

        if( allrelation.size() == 0){
            return ResponseUtil.fail();
        }
        return ResponseUtil.ok(allrelation);
    }
    // 返回数据库中所有可能出现的关系
    @GetMapping("/queryAllRelationType")
    public Object queryAllRelationType(){
        return ResponseUtil.ok(visService.getAllRelationTypeSet());
    }


    //增加关系 (*目前只能对已有节点进行添加关系操作)(***目前只能操作"关系三元组"表的数据)
    @PostMapping("/addRelationTuple")
    public Object addRelationTuple(@RequestBody RelationTuple newRelationTuple){
        //判断节点是否存在
        if (!((visService.checkNodeInfoExist(newRelationTuple.getChildId()) && visService.checkNodeInfoExist(newRelationTuple.getFatherId())))){
            return ResponseUtil.fail(-1, "The Node doesn't existed!");
        }
        //判断该关系是否存在
        if (visService.checkRelationExist(newRelationTuple)){
            return ResponseUtil.fail(-1,"This relaiton has already existed!");
        }

        //添加关系
        if(visService.addNewRelation(newRelationTuple)==1){
            return ResponseUtil.ok();
        }
        else{
            return ResponseUtil.fail();
        }
    }
    //删除关系 (***目前只能操作"关系三元组"表的数据)
    @PostMapping("/deleteRelationTuple")
    public Object deleteRelationTuple(@RequestBody RelationTuple targetRelationTuple){
        //判断该关系是否存在
        if (!(visService.checkRelationExist(targetRelationTuple))){
            return ResponseUtil.fail(-1,"This relaiton doesn't exist!");
        }
        else if(visService.deleteExistRelation(targetRelationTuple) == 1){
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }
    //更改关系 (***目前只能操作"关系三元组"表的数据)
    @PostMapping("/changeRelation/{newRelationName}")
    public Object changeRelaiton(@PathVariable String newRelationName,
                                 @RequestBody RelationTuple oldRelationTuple){
        // 判断该关系是否存在
        if (!(visService.checkRelationExist(oldRelationTuple))){
            return ResponseUtil.fail(-1,"This relaiton doesn't exist!");
        }

        if(visService.updateRelation(oldRelationTuple, newRelationName) == 1){
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail();
    }

    //给定两个节点的<名字>  返回两者之间所有的关系   (*注意这个接口 如果节点重名它会返回所有符合名字要求的关系)
    @GetMapping("queryRelationTupleByTwoName/{node1Name}/{node2Name}")
    public Object queryRelationTupleByTwoName(@PathVariable String node1Name,
                                              @PathVariable String node2Name){
        List<RelationTuple> result = visService.getRelationByFatherNameandChildName(node1Name, node2Name);
        result.addAll(visService.getRelationByFatherNameandChildName(node2Name, node1Name));
        return ResponseUtil.ok(result);
    }
    //给定父节点<名字> 子节点<名字> 返回关系
    @GetMapping("queryRelationTupleByFatherNameChildName/{fatherName}/{childName}")
    public Object queryRelationTupleByFatherNameChildName(@PathVariable String fatherName,
                                                          @PathVariable String childName){
        List<RelationTuple> result = visService.getRelationByFatherNameandChildName(fatherName, childName);
        return ResponseUtil.ok(result);
    }

    //根据id返回对应节点的一阶关系
    @GetMapping("/getOneStageNodeRelationTupleById/{requestNodeId}")
    public Object getOneStageNodeRelationTupleById(@PathVariable String requestNodeId){
        List<RelationTuple> result = visService.getOneStageNodeRelationTuple(requestNodeId);
        return ResponseUtil.ok(result);
    }

    //根据id返回对应节点的一二阶关系(不太行  二阶节点与一阶节点关系网没弄)
    @GetMapping("/getTwoStageNodeRelationTupleById/{requestNodeId}")
    public Object getTwoStageNodeRelationTupleById(@PathVariable String requestNodeId){
        List<RelationTuple> result = visService.getTwoStageNodeRelationTuple(requestNodeId);
        return ResponseUtil.ok(result);
    }

    //根据节点<名字>查询他的所有属性(存在同name不同node情况 ---> 返回多个对象)
    @GetMapping("/getNodeAttributeByName/{requestNodeName}")
    public Object getNodeAttributeByName(@PathVariable String requestNodeName){
        List<NodeInfo> backNodeList= visService.getANodeInfoByName(requestNodeName);
        if (backNodeList.size() == 0){
            return ResponseUtil.fail(-1,"No node match this name!");
        }
        else {
            ResultList resultlist = new ResultList();

            resultlist.setUnitsequenceList(new ArrayList<>());
            resultlist.setCharacterDataList(new ArrayList<>());
            resultlist.setEquipmentList(new ArrayList<>());

            for (NodeInfo backNode : backNodeList) {
                switch (backNode.getLabel()) {
                    case 0:
                        List<UnitSequence> unitResult = visService.getUnitSequenceByName(requestNodeName);
                        resultlist.addUnitSequence(unitResult);
                        break;
                    case 1:
                        List<CharacterData> charcterResult = visService.getCharacterDataByName(requestNodeName);
                        resultlist.addCharacterData(charcterResult);
                        break;
                    case 2:
                        List<EquipmentTree> equipmentResult = visService.getEquipmentTreeByName(requestNodeName);
                        resultlist.addEquipmentTree(equipmentResult);
                        break;
                }
            }
            return ResponseUtil.ok(resultlist);
        }
    }
    //根据<节点Id>查询一个节点的所有属性(节点为唯一标识-->只返回一个对象)
    @GetMapping("/getNodeAttributeById/{requestNodeId}")
    public Object getNodeAttributeById(@PathVariable String requestNodeId){
        NodeInfo backNode = visService.getANodeInfoById(requestNodeId);
        if (backNode == null){
            return ResponseUtil.fail(-1,"No node match this name!");
        }
        switch (backNode.getLabel()) {
            case 0:
                UnitSequence unitResult = visService.getUnitSequenceById(requestNodeId);
                return ResponseUtil.ok(unitResult);
            case 1:
                CharacterData characterResult = visService.getCharacterDataById(requestNodeId);
                return ResponseUtil.ok(characterResult);
            case 2:
                EquipmentTree equipmentResult = visService.getEquipmentTreeById(requestNodeId);
                return ResponseUtil.ok(equipmentResult);
        }
        return ResponseUtil.fail();
    }


    //增加 Unit 节点
    @PostMapping("addUnitSequenceNode")
    public Object addUnitSequenceNode(@RequestBody UnitSequence unitSequence){
        if (unitSequence.getUnitId() == null){
            return ResponseUtil.fail(-1,"New Unit Sequence must has unitId!");
        }
        //检查节点是否存在
        if (visService.checkNodeInfoExist(unitSequence.getUnitId())){
            return ResponseUtil.fail(-1,"This Unit Node has already existed!");
        }
        else {
            if(visService.addAUnitSequenceNode(unitSequence)){
                return ResponseUtil.ok();
            }
            return ResponseUtil.fail();
        }
    }
    //增加 CharacterData 节点
    @PostMapping("addCharacterDataNode")
    public Object addCharacterDataNode(@RequestBody CharacterData characterData){
        if (characterData.getPersonId() == null){
            return ResponseUtil.fail(-1, "New CharacterData must has personId!");
        }
        //检查节点是否存在
        if (visService.checkNodeInfoExist(characterData.getPersonId())){
            return ResponseUtil.fail(-1,"This Person Node has already existed!");
        }
        else {
            if(visService.addACharacterDataNode(characterData)){
                return ResponseUtil.ok();
            }
            return ResponseUtil.fail();
        }
    }
    //增加 EquipmentTree 节点
    @PostMapping("addEquipmentTreeNode")
    public Object addEquipmentTreeNode(@RequestBody EquipmentTree equipmentTree){
        if (equipmentTree.getEquipmentId() == null){
            return ResponseUtil.fail(-1, "New EquipmentTree must has equipmentTreeId!");
        }
        //检查节点是否存在
        if (visService.checkNodeInfoExist(equipmentTree.getEquipmentId())){
            return ResponseUtil.fail(-1,"This Equipment Node has already existed!");
        }
        else {
            if(visService.addAEquipmentTreeNode(equipmentTree)){
                return ResponseUtil.ok();
            }
            return ResponseUtil.fail();
        }
    }

    //删除 Unit 节点
    @PostMapping("deleteUnitSequenceNode")
    public Object deleteUnitSequenceNode(@RequestBody UnitSequence unitSequence){
        //检查节点是否存在
        if (!(visService.checkNodeInfoExist(unitSequence.getUnitId()))){
            return ResponseUtil.fail(-1,"This Unit Node doesn't existed!");
        }
        else{
            if(visService.deleteAUnitSequenceNode(unitSequence)){
                return ResponseUtil.ok();
            }
            return ResponseUtil.fail();
        }
    }
    //删除 CharacterData 节点
    @PostMapping("deleteCharacterDataNode")
    public Object deleteCharacterDataNode(@RequestBody CharacterData characterData){
        //检查节点是否存在
        if (!(visService.checkNodeInfoExist(characterData.getPersonId()))){
            return ResponseUtil.fail(-1,"This Person Node doesn't existed!");
        }
        else{
            if(visService.deleteACharacterDataNode(characterData)){
                return ResponseUtil.ok();
            }
            return ResponseUtil.fail();
        }
    }
    //删除 EquipmentTree 节点
    @PostMapping("deleteEquipmentTreeNode")
    public Object deleteEquipmentTreeNode(@RequestBody EquipmentTree equipmentTree){
        //检查节点是否存在
        if (!(visService.checkNodeInfoExist(equipmentTree.getEquipmentId()))){
            return ResponseUtil.fail(-1,"This Equipment Node doesn't existed!");
        }
        else{
            if(visService.deleteAEquipmentTreeNode(equipmentTree)){
                return ResponseUtil.ok();
            }
            return ResponseUtil.fail();
        }
    }



    // 根据id 修改对应节点的属性值
    @PostMapping("/changeNodeSomeAttributeById/{requestId}")
    public Object changeNodeSomeAttributeById(@PathVariable String requestId,
                                              @RequestBody Map<String,String> AttributeObject){
        NodeInfo requestNodeInfo = visService.getANodeInfoById(requestId);
        if (requestNodeInfo == null){
            return ResponseUtil.fail(-1,"No node match this ID!");
        }
        else{//*目前只是简单修改功能的实现,没有考虑“单位序列”“装备类别””装备配赋“三个表中数据的联动---->虽然能跑通 但还没写完
            if(requestNodeInfo.getLabel() == 0){//更改Unit属性
                if(visService.modifyUnitSequenceAttributeValue(requestId, AttributeObject)==1){
                    return ResponseUtil.ok();
                }
                else{
                    return ResponseUtil.fail();
                }
            }
            else if(requestNodeInfo.getLabel() == 1){//更改Person属性
                if (visService.modifyCharacterDataAttributeValue(requestId, AttributeObject)==1){
                    return ResponseUtil.ok();
                }
                else {
                    return ResponseUtil.fail();
                }
            }
            else if(requestNodeInfo.getLabel() == 2){//更改EquipmentTree属性
                if (visService.modifyEquipmentTreeAttributeValue(requestId, AttributeObject) == 1){
                    return ResponseUtil.ok();
                }
                else {
                    return ResponseUtil.fail();
                }
            }
        }
        return ResponseUtil.fail();
    }

    //查询表头
    @GetMapping("/showRelationTupleColumn")
    public Object showRelationTupleColumn(){
        return ResponseUtil.ok(relationTupleMapper.showAllColumn());
    }




//    @PostMapping("/updateAttribute/{id}")
//    public Object updateAttribute(@PathVariable("id") Integer id,@RequestBody Map<String,Object> map){
//        NodeInfo nodeInfo = nodeInfoMapper.getNodeInfoById(id);
//        String tableName = nodeTypeMapper.getTableNameByNodeTypeId(nodeInfo.getNodeTypeId());
//        if (attributeMapper.updateAttribute(tableName,(Integer) map.get("id"),map) == 1){
//            return ResponseUtil.ok();
//        } else {
//            return ResponseUtil.fail();
//        }
//    }


//    public Object changeNodeAttributeBy(){
//
//    }
/* ------------------------------------------------------------------------------------------------------------*/


//    @GetMapping("/getEquipmentTypeLevelRelation")
//    public Object getEquipmentTypeLevelRelation(){
//        List<HashMap> res = visService.getEquipmentTypeLevelRelation();
//        if (res.size() == 0){
//            return ResponseUtil.fail();
//        } else{
//            return ResponseUtil.ok(res);
//        }
//    }
//
//    @GetMapping("/getEquipmentTreeLevelRelation")
//    public Object getEquipmentTreeLevelRelation(){
//        List<HashMap> res = visService.getEquipmentTreeLevelRelation();
//        if (res.size() == 0){
//            return ResponseUtil.fail();
//        } else{
//            return ResponseUtil.ok(res);
//        }
//    }
//
//    @GetMapping("/getUnitSequenceLevelRelation")
//    public Object getUnitSequenceLevelRelation(){
//        List<HashMap> res = visService.getUnitSequenceLevelRelation();
//        if (res.size() == 0){
//            return ResponseUtil.fail();
//        } else{
//            return ResponseUtil.ok(res);
//        }
//    }
//
//    /**
//     * 添加装备类型
//     * @param equipmentType
//     * @return
//     */
//    @Transactional(rollbackFor = Exception.class)
//    @PostMapping("/addEquipmentType")
//    public Object addEquipmentType(@RequestBody EquipmentType equipmentType){
//        if(equipmentTypeMapper.insertEquipmentType(equipmentType) == 1){
//            return ResponseUtil.ok();
//        } else {
//            return ResponseUtil.fail();
//        }
//    }
//
//    /**
//     * 删除装备类型
//     * @param name
//     * @return
//     */
//    @PostMapping("/deleteEquipmentType/{name}")
//    public Object deleteEquipmentType(@PathVariable("name") String name){
//        if(equipmentTypeMapper.deleteEquipmentType(name) == 1){
//            return ResponseUtil.ok();
//        } else {
//            return ResponseUtil.fail();
//        }
//    }
//
//    /**
//     * 添加装备树
//     * @param equipmentTree
//     * @return
//     */
//    @Transactional(rollbackFor = Exception.class)
//    @PostMapping("/addEquipmentTree")
//    public Object addEquipmentTree(@RequestBody EquipmentTree equipmentTree){
//        if(equipmentTreeMapper.insertEquipmentTree(equipmentTree)==1){
//            return ResponseUtil.ok();
//        } else {
//            return ResponseUtil.fail();
//        }
//    }
//
//    /**
//     * 删除装备树
//     * @param name
//     * @return
//     */
//    @PostMapping("/deleteEquipmentTree/{name}")
//    public Object deleteEquipmentTree(@PathVariable("name") String name){
//        if(equipmentTreeMapper.deleteEquipmentTree(name) == 1){
//            return ResponseUtil.ok();
//        } else {
//            return ResponseUtil.fail();
//        }
//    }
//
//    /**
//     * 添加单位
//     * @param unitSequence
//     * @return
//     */
//    @Transactional(rollbackFor = Exception.class)
//    @PostMapping("/addUnitSequence")
//    public Object addUnitSequence(@RequestBody UnitSequence unitSequence){
//        if(unitSequenceMapper.insertUnitSequence(unitSequence) == 1){
//            return ResponseUtil.ok();
//        } else {
//            return ResponseUtil.fail();
//        }
//    }
//
//    /**
//     * 删除单位
//     * @param name
//     * @return
//     */
//    @PostMapping("/deleteUnitSequence/{name}")
//    public Object deleteUnitSequence(@PathVariable("name") String name){
//        if(unitSequenceMapper.deleteUnitSequence(name) == 1){
//            return ResponseUtil.ok();
//        } else{
//            return ResponseUtil.fail();
//        }
//    }
//
//    /**
//     * 获取表中name对应的记录
//     * @param table
//     * @param name
//     * @return
//     */
//    @GetMapping("/getAttributeByTableAndName/{table}/{name}")
//    public Object getAttributeByTableAndName(@PathVariable("table") String table,
//                                             @PathVariable("name") String name){
//        List<HashMap> result = visService.objectDataSwitch(table, name);
//
//        if(result.size() == 0){
//            return ResponseUtil.fail();
//        } else{
//            return ResponseUtil.ok(result);
//        }
//    }
//
//    /**
//     * 获取表格拥有的属性
//     * @param table
//     * @return
//     */
//    @GetMapping("/getEmptyAttributeByTable/{table}")
//    public Object getEmptyAttributeByTable(@PathVariable("table") String table){
//        List<HashMap> result = new ArrayList<>();
//        List<String> attributeNames = visService.objectAttribute(table);
//
//        for (String attributeName :attributeNames) {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("attributeName", attributeName);
//            map.put("content", "");
//            result.add(map);
//        }
//
//        if(result.size() == 0){
//            return ResponseUtil.fail();
//        } else{
//            return ResponseUtil.ok(result);
//        }
//    }
//
//    /**
//     * 获取节点和其上一级节点
//     * @param nodeName
//     * @return
//     */
//    @GetMapping("queryNodeRelationByName/{nodeName}")
//    public Object queryNodeByName(@PathVariable("nodeName") String nodeName){
//        List<HashMap> result = new ArrayList<>();
//        Object obj = null;
//        String nameInfo = "";
//        UnitSequence unitSequence = unitSequenceMapper.getUnitSequenceByName(nodeName);
//        EquipmentTree equipmentTree = equipmentTreeMapper.getEquipmentByName(nodeName);
//        BiographicalInformation biographicalInformation = (BiographicalInformation) biographicalInformationMapper.getBiographicalInformationByName(nodeName); //这一句务必检查一下
//
//        if(unitSequence !=null){
//            obj = unitSequence;
//            nameInfo = "unitSequence";
//        } else if(equipmentTree != null){
//            obj = equipmentTree;
//            nameInfo = "equipmentTree";
//        } else if(biographicalInformation !=null){
//            obj = biographicalInformation;
//            nameInfo = "biographicalInformation";
//        }
//
//        switch (nameInfo){
//            case "unitSequence":
//                result.add(visService.getUnitSequenceRelation((UnitSequence) obj));
//                break;
//            case "equipmentTree":
//                result.add(visService.getEquipmentRelation((EquipmentTree) obj));
//                break;
//            case "biographicalInformation":
//                result.add(visService.getBiographicalInformationRelation((BiographicalInformation) obj));
//                break;
//            default:   // 加了一句 default
//        }
//
//        if(result.size()==0){
//            return ResponseUtil.fail();
//        } else{
//            return ResponseUtil.ok(result);
//        }
//
//    }
//
//    /**
//     * 获取节点和其上一节点的所有Label
//     * @param nodeName
//     * @return
//     */
//    @GetMapping("/queryNodeLabelByName/{nodeName}")
//    public Object queryNodeLabelByName(@PathVariable("nodeName") String nodeName){
//        List<HashMap> result = null;
//
//        String nameInfo = "";
//        Object obj = null;
//        UnitSequence unitSequence = unitSequenceMapper.getUnitSequenceByName(nodeName);
//        EquipmentTree equipmentTree = equipmentTreeMapper.getEquipmentByName(nodeName);
//        BiographicalInformation biographicalInformation = (BiographicalInformation) biographicalInformationMapper.getBiographicalInformationByName(nodeName);
//
//        if(unitSequence !=null){
//            obj = unitSequence;
//            nameInfo = "unitSequence";
//        } else if(equipmentTree != null){
//            obj = equipmentTree;
//            nameInfo = "equipmentTree";
//        } else if(biographicalInformation !=null){
//            obj = biographicalInformation;
//            nameInfo = "biographicalInformation";
//        }
//
//        switch (nameInfo){
//            case "unitSequence":
//                result = visService.getUnitSequenceLabel((UnitSequence) obj);
//                break;
//            case "equipmentTree":
//                result = visService.getEquipmentLabel((EquipmentTree) obj);
//                break;
//            case "biographicalInformation":
//                result = visService.getBiographicalInformationLabel((BiographicalInformation) obj);
//                break;
//            default:   // 加了一句 default
//        }
//
//        if(result.size()==0){
//            return ResponseUtil.fail();
//        } else{
//            return ResponseUtil.ok(result);
//        }
//    }
//
//    /**
//     * 获取节点间路径 -- 父子节点和关系
//     * @param nodeName1
//     * @param nodeName2
//     * @return
//     */
//    @GetMapping("/queryNodeRelationBetweenTwoNodes/{nodeName1}/{nodeName2}")
//    public Object queryNodeRelationBetweenTwoNodes(@PathVariable("nodeName1") String nodeName1,
//                                                   @PathVariable("nodeName2") String nodeName2){
//        List<HashMap> result = new ArrayList<HashMap>();
//        String nodeNameInfo1 = visService.judgeNodeName(nodeName1);
//        if (nodeNameInfo1.equals("unitSequence")){
//            result = visService.getRelationBetweenUnitAndUnit(nodeName1, nodeName2);
//        } else if(nodeNameInfo1.equals("equipmentTree")){
//            result = visService.getRelationBetweenEquipmentAndUnit(nodeName1, nodeName2);
//        } else if(nodeNameInfo1.equals("biographicalInformation")){
//            result = visService.getRelationBetweenBiographicalAndUnit(nodeName1, nodeName2);
//        }
//
//        if(result.size() == 0){
//            return ResponseUtil.fail();
//        }
//        return ResponseUtil.ok();
//    }
//
//    /**
//     * 获取节点间路径带Label
//     * @param nodeName1
//     * @param nodeName2
//     * @return
//     */
//    @GetMapping("/queryNodeLabelBetweenTwoNodes/{nodeName1}/{nodeName2}")
//    public Object queryNodeLabelBetweenTwoNodes(@PathVariable("nodeName1") String nodeName1,
//                                                @PathVariable("nodeName2") String nodeName2){
//        List<HashMap> result = new ArrayList<HashMap>();
//        String nodeNameInfo1 = visService.judgeNodeName(nodeName1);
//        if (nodeNameInfo1.equals("unitSequence")){
//            result = visService.getLabelBetweenUnitAndUnit(nodeName1, nodeName2);
//        } else if(nodeNameInfo1.equals("equipmentTree")){
//            result = visService.getLabelBetweenEquipmentAndUnit(nodeName1, nodeName2);
//        } else if(nodeNameInfo1.equals("biographicalInformation")){
//            result = visService.getLabelBetweenBiographicalAndUnit(nodeName1, nodeName2);
//        }
//
//        if(result.size() == 0){
//            return ResponseUtil.fail();
//        }
//        return ResponseUtil.ok();
//    }
//
//    @GetMapping("/queryAllNodesRelationship")
//    public Object queryAllNodesRelationship(){
//        List<HashMap> result = visService.getAllNodesRelationship();
//        if (result.size() == 0){
//            return ResponseUtil.fail();
//        }
//        return ResponseUtil.ok(result);
//    }
//
//    @GetMapping("/queryAllNodesWithLabel")
//    public Object queryAllNodesWithLabel(){
//        List<HashMap> result = visService.getAllNodesWithLabel();
//        if (result.size() == 0){
//            return ResponseUtil.fail();
//        }
//        return ResponseUtil.ok(result);
//    }
//
//    @GetMapping("/getNodeRelationBelongToGivenNode/{initNode}")
//    public Object getNodeRelationBelongToGivenNode(@PathVariable("initNode") String initNode){
//        List<HashMap> result = new ArrayList<HashMap>();
//        List<String> nodeList = new ArrayList<String>(Arrays.asList(initNode));
//        List<String> relationTypeList = new ArrayList<String >(Arrays.asList(relationTypes.split(",")));
//
//        while(!nodeList.isEmpty()){
//            List<String> tempNodeList = new ArrayList<String>();
//            for(int i = 0; i < nodeList.size(); i++){
//                String nodeName = nodeList.get(i);
//                for(int j = 0; j< relationTypeList.size(); j++){
//                    String nodeRelationType = relationTypeList.get(i);
//                    List<String> childrenNames = visService.getChildrenNodeNameByFatherNode(nodeName, nodeRelationType);
//                    if(!childrenNames.isEmpty()){
//                        for(int k=0; k<childrenNames.size();k++){
//                            String childName = childrenNames.get(k);
//                            tempNodeList.add(childName);
//
//                            HashMap<String, Object> map = new HashMap<String , Object>();
//                            map.put("fatherNode", nodeName);
//                            map.put("childNode", childName);
//                            map.put("nodeRelationType", nodeRelationType);
//                            result.add(map);
//                        }
//                    }
//                }
//            }
//            nodeList.clear();
//            nodeList.addAll(tempNodeList);
//        }
//        return ResponseUtil.ok(result);
//    }
//
//    @GetMapping("/getNodeLabelBelongToGivenNode/{initNode}")
//    public Object getNodeLabelBelongToGivenNode(@PathVariable("initNode") String initNode){
//        List<HashMap> result = new ArrayList<HashMap>();
//        List<String> nodeList = new ArrayList<String>(Arrays.asList(initNode));
//        List<String> relationTypeList = new ArrayList<String >(Arrays.asList(relationTypes.split(",")));
//
//        while(!nodeList.isEmpty()){
//            List<String> tempNodeList = new ArrayList<String >();
//            for (int i = 0; i < nodeList.size(); i++){
//                String nodeName = nodeList.get(i);
//                Object label = nodeLabelMapper.getLabelByName(nodeName);
//
//                HashMap<String , Object> map = new HashMap<String, Object>();
//                map.put("nodeName", nodeName);
//                map.put("label", (int) label);
//                result.add(map);
//
//                for(int j = 0; j<relationTypeList.size();j++){
//                    String nodeRelationType = relationTypeList.get(i);
//                    List<String> childrenNames = visService.getChildrenNodeNameByFatherNode(nodeName, nodeRelationType);
//                    if(!childrenNames.isEmpty()){
//                        for(int k =0; k<childrenNames.size();k++){
//                            String childName = childrenNames.get(k);
//                            tempNodeList.add(childName);
//                        }
//                    }
//                }
//            }
//            nodeList.clear();
//            nodeList.addAll(tempNodeList);
//        }
//        if(result.size()==0){
//            return ResponseUtil.fail();
//        }
//        return ResponseUtil.ok(result);
//    }
//
//    @GetMapping("/getAttributeValueByNodeName/{name}")
//    public Object getAttributeValueByNodeName(@PathVariable("name") String name){
//        Object nodeInfo = nodeInfoMapper.getNodeInfoByName(name);
//        if(nodeInfo == null){
//            return ResponseUtil.fail();
//        }
//        Object tableName = nodeTypeMapper.getTableNameByNodeTypeId(((NodeInfo) nodeInfo).getNodeTypeId());
//        if(tableName == null){
//            return ResponseUtil.fail();
//        }
//        Map<String, Object> map = attributeMapper.getAttributeById((String) tableName, ((NodeInfo) nodeInfo).getAttributeId());
//        map.remove("id");
//        if(map.size() == 0){
//            return ResponseUtil.fail();
//        } else {
//            return ResponseUtil.ok(JSONObject.fromObject(map));
//        }
//    }

    // 以下是旧版函数，已经注释掉了
    //===================以下为旧版函数============================

//    /**
//     * 获得默认显示节点
//     * @return
//     */
//    @GetMapping("/initNodes")
//    public Object getInitNodes() {
//        NodeInfo nodeInfo = nodeInfoMapper.getNodeInfoByName(initTeamName);
//        if (nodeInfo == null) {
//            return ResponseUtil.fail();
//        }
//        List<ResNode> resNodes = visService.getTwoLevelNode(nodeInfo);
//        if (resNodes.size() == 0) {
//            return ResponseUtil.fail();
//        }
//        return ResponseUtil.ok(resNodes);
//    }
//
//    /**
//     * 获得默认显示联系
//     * @return
//     */
//    @GetMapping("/initRelations")
//    public Object getInitRelations() {
//        NodeInfo nodeInfo = nodeInfoMapper.getNodeInfoByName(initTeamName);
//        if (nodeInfo == null) {
//            return ResponseUtil.fail();
//        }
//        List<ResRelation> resRelations = visService.getTwoLevelRelation(nodeInfo);
//        if (resRelations.size() == 0) {
//            return ResponseUtil.fail();
//        }
//        return ResponseUtil.ok(resRelations);
//    }
//
//    /**
//     * 根据nodeInfoId查询，返回该id对应节点的各种属性
//     * @param id
//     * @return
//     */
//    @GetMapping("/getAttribute/{id}")
//    public Object getAttribute(@PathVariable("id") Integer id) {
//        NodeInfo nodeInfo = nodeInfoMapper.getNodeInfoById(id);
//        String tableName = nodeTypeMapper.getTableNameByNodeTypeId(nodeInfo.getNodeTypeId());
//        Map<String,Object> map = attributeMapper.getAttributeById(tableName,nodeInfo.getAttributeId());
//        map.remove("id");
//        if (map.size() == 0) {
//            return ResponseUtil.fail();
//        } else {
//            return ResponseUtil.ok(JSONObject.fromObject(map));
//        }
//    }
//
//    /**
//     * 根据姓名获得全部属性
//     * @param name
//     * @return
//     */
//    @GetMapping("/getAttributeByName/{name}")
//    public Object getAttributeByName(@PathVariable("name") String name) {
//        NodeType nodeType = nodeTypeMapper.getNodeTypeByName(name);
//        if (nodeType == null) {
//            return ResponseUtil.fail();
//        }
//        List<String> attributes = attributeMapper.getAllAttributeByName(nodeType.getAttributeTableName());
//        if (attributes.size() == 0) {
//            return ResponseUtil.fail();
//        } else {
//            return ResponseUtil.ok(attributes);
//        }
//    }
//
//
//    /**
//     * 根据名字添加相应的属性
//     * @param name
//     * @param attributeName
//     * @return
//     */
//    @PostMapping("/addAttribute/{name}/{attributeName}")
//    public Object addAttribute(@PathVariable("name") String name, @PathVariable("attributeName") String attributeName){
//        NodeType nodeType = nodeTypeMapper.getNodeTypeByName(name);
//        String tableName = nodeType.getAttributeTableName();
//        attributeMapper.addAttribute(tableName,attributeName);
//        List<String> attributes = attributeMapper.getAllAttributeByName(tableName);
//        if (attributes.contains(attributeName)) {
//            return ResponseUtil.ok();
//        } else {
//            return ResponseUtil.fail();
//        }
//    }
//
//    /**
//     * 根据姓名删除属性
//     * @param name
//     * @param attributeName
//     * @return
//     */
//    @PostMapping("/deleteAttribute/{name}/{attributeName}")
//    public Object deleteAttribute(@PathVariable("name") String name, @PathVariable("attributeName") String attributeName){
//        NodeType nodeType = nodeTypeMapper.getNodeTypeByName(name);
//        String tableName = nodeType.getAttributeTableName();
//        attributeMapper.deleteAttribute(tableName,attributeName);
//        List<String> attributes = attributeMapper.getAllAttributeByName(tableName);
//        if (attributes.contains(attributeName)) {
//            return ResponseUtil.fail();
//        } else {
//            return ResponseUtil.ok();
//        }
//    }
//
//    /**
//     * 修改数据库的表的一列
//     * @param name
//     * @param oldName
//     * @param newName
//     * @return
//     */
//    @PostMapping("/updateAttribute/{name}/{oldName}/{newName}")
//    public Object updateAttributeByName(@PathVariable("name") String name,
//                                        @PathVariable("oldName") String oldName,
//                                        @PathVariable("newName") String newName) {
//        NodeType nodeType = nodeTypeMapper.getNodeTypeByName(name);
//        String tableName = nodeType.getAttributeTableName();
//        attributeMapper.updateColumn(tableName,oldName,newName);
//        List<String> attributes = attributeMapper.getAllAttributeByName(tableName);
//        if (attributes.contains(newName) && !attributes.contains(oldName)) {
//            return ResponseUtil.ok();
//        } else {
//            return ResponseUtil.fail();
//        }
//    }
//
//    /**
//     * 根据id更新属性
//     * @param id
//     * @param map
//     * @return
//     */
//    @PostMapping("/updateAttribute/{id}")
//    public Object updateAttribute(@PathVariable("id") Integer id,@RequestBody Map<String,Object> map){
//        NodeInfo nodeInfo = nodeInfoMapper.getNodeInfoById(id);
//        String tableName = nodeTypeMapper.getTableNameByNodeTypeId(nodeInfo.getNodeTypeId());
//        if (attributeMapper.updateAttribute(tableName,(Integer) map.get("id"),map) == 1){
//            return ResponseUtil.ok();
//        } else {
//            return ResponseUtil.fail();
//        }
//    }
//
//    @GetMapping("/getLevelRelation")
//    public Object getLevelRelation(){
//        List<ResNodeType> res = visService.getLevelRelation();
//        if (res.size() == 0) {
//            return ResponseUtil.fail();
//        } else {
//            return ResponseUtil.ok(res);
//        }
//    }
//
//    /**
//     * 添加节点类型
//     * @param nodeType
//     * @return
//     */
//    @Transactional(rollbackFor = Exception.class)
//    @PostMapping("/addNodeType")
//    public Object addNodeType(@RequestBody NodeType nodeType) {
//        if (nodeTypeMapper.insertNodeType(nodeType) == 1) {
//            if (attributeMapper.existTable(nodeType.getAttributeTableName()) == 0) {
//                attributeMapper.createTable(nodeType.getAttributeTableName());
//            } else {
//                return ResponseUtil.fail();
//            }
//            return ResponseUtil.ok();
//        } else {
//            return ResponseUtil.fail();
//        }
//    }
//
//    /**
//     * 删除节点类型
//     * @param name
//     * @return
//     */
//    @PostMapping("/deleteNodeType/{name}")
//    public Object deleteNodeType(@PathVariable("name") String name) {
//        NodeType nodeType = nodeTypeMapper.getNodeTypeByName(name);
//        attributeMapper.deleteTable(nodeType.getAttributeTableName());
//        if (attributeMapper.existTable(nodeType.getAttributeTableName()) != 0) {
//            return ResponseUtil.fail();
//        }
//        if (nodeTypeMapper.deleteNodeType(name) == 1) {
//            return ResponseUtil.ok();
//        } else {
//            return ResponseUtil.fail();
//        }
//    }
//
//    /**
//     * 根据姓名获取一级关系节点
//     * @param name
//     * @return
//     */
//    @GetMapping("/getOneLevelNode/{name}")
//    public Object getOneLevelNodeByName(@PathVariable("name") String name) {
//        NodeInfo nodeInfo = nodeInfoMapper.getNodeInfoByName(name);
//        if (nodeInfo == null) {
//            return ResponseUtil.fail();
//        }
//        List<ResNode> resNodes = visService.getOneLevelNode(nodeInfo);
//        if (resNodes.size() == 0) {
//            return ResponseUtil.fail();
//        }
//        return ResponseUtil.ok(resNodes);
//    }
//
//    /**
//     * 根据姓名获取一级关系连接
//     * @param name
//     * @return
//     */
//    @GetMapping("/getOneLevelRelation/{name}")
//    public Object getOneLevelRelation(@PathVariable("name") String name) {
//        NodeInfo nodeInfo = nodeInfoMapper.getNodeInfoByName(name);
//        if (nodeInfo == null) {
//            return ResponseUtil.fail();
//        }
//        List<ResRelation> resRelations = visService.getOneLevelRelation(nodeInfo);
//        if (resRelations.size() == 0) {
//            return ResponseUtil.fail();
//        }
//        return ResponseUtil.ok(resRelations);
//    }
//
//    /**
//     * 根据姓名获取二级关系节点
//     * @param name
//     * @return
//     */
//    @GetMapping("/getTwoLevelNode/{name}")
//    public Object getTwoLevelNodeByName(@PathVariable("name") String name) {
//        NodeInfo nodeInfo = nodeInfoMapper.getNodeInfoByName(name);
//        if (nodeInfo == null) {
//            return ResponseUtil.fail();
//        }
//        List<ResNode> resNodes = new ArrayList<>();
//        if (nodeInfo.getNodeTypeId() == playerType) {
//            NodeRelationInfo nodeRelationInfo = nodeRelationInfoMapper.getNodeRelationByNode1(nodeInfo.getNodeInfoId());
//            NodeInfo nodeInfo1 = nodeInfoMapper.getNodeInfoById(nodeRelationInfo.getNodeInfoId2());
//            resNodes = visService.getTwoLevelNode(nodeInfo1);
//        } else if (nodeInfo.getNodeTypeId() == teamType) {
//            resNodes = visService.getTwoLevelNode(nodeInfo);
//        }
//        if (resNodes.size() == 0) {
//            return ResponseUtil.fail();
//        }
//        return ResponseUtil.ok(resNodes);
//    }
//
//    /**
//     * 根据姓名获取二级关系联系
//     * @param name
//     * @return
//     */
//    @GetMapping("/getTwoLevelRelation/{name}")
//    public Object getTwoLevelRelation(@PathVariable("name") String name) {
//        NodeInfo nodeInfo = nodeInfoMapper.getNodeInfoByName(name);
//        if (nodeInfo == null) {
//            return ResponseUtil.fail();
//        }
//        List<ResRelation> resRelations = new ArrayList<>();
//        if (nodeInfo.getNodeTypeId() == playerType) {
//            NodeRelationInfo nodeRelationInfo = nodeRelationInfoMapper.getNodeRelationByNode1(nodeInfo.getNodeInfoId());
//            NodeInfo nodeInfo1 = nodeInfoMapper.getNodeInfoById(nodeRelationInfo.getNodeInfoId2());
//            resRelations = visService.getTwoLevelRelation(nodeInfo1);
//        } else if (nodeInfo.getNodeTypeId() == teamType) {
//            resRelations = visService.getTwoLevelRelation(nodeInfo);
//        }
//        if (resRelations.size() == 0) {
//            return ResponseUtil.fail();
//        }
//        return ResponseUtil.ok(resRelations);
//    }
//
//    @GetMapping("/getAllNodeInVis")
//    public Object getAllNodeInVis() {
//        String initHeadQuarters = "太平洋舰队司令部";
//        String relationTypes = "下属";
//        int label = 0;
//        List<HashMap> result = new ArrayList<HashMap>();
//
//        List<String> nodeList = new ArrayList<String>(Arrays.asList(initHeadQuarters));
//        List<String> relationTypeList = new ArrayList<String>(Arrays.asList(relationTypes));
//
//        while(!nodeList.isEmpty()) {
//            List<String> tempNodeList = new ArrayList<String>();
//            for (int i = 0; i < nodeList.size(); i++) {
//                String nodeName = nodeList.get(i);
//                for (int j = 0; j < relationTypeList.size(); j++) {
//                    String nodeRelationType = relationTypeList.get(j);
//                    List<String> childrenName = visService.getNodeNameByFatherNodeAndRelation(nodeName, nodeRelationType);
//                    if (!childrenName.isEmpty()) {
//                        for (int k = 0; k < childrenName.size(); k++) {
//                            String childName = childrenName.get(k);
//                            tempNodeList.add(childName);
//
//                            HashMap<String, Object> map = new HashMap<String,Object>();
//                            map.put("fatherNode", nodeName);
//                            map.put("childNode", childName);
//                            map.put("label", label);
//                            map.put("nodeRelationType", nodeRelationType);
//                        }
//                    }
//                }
//            }
//            label = label + 1;
//            nodeList.clear();
//            nodeList.addAll(tempNodeList);
//        }
//        if (result.size() == 0) {
//            return ResponseUtil.fail();
//        } else {
//            return ResponseUtil.ok(result);
//        }
//    }
}
