package xmu.vis.service;

import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleSizeExpr;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sun.security.krb5.internal.crypto.HmacSha1Aes128CksumType;
import xmu.vis.domain.*;
import xmu.vis.mapper.*;
import xmu.vis.utils.ResponseUtil;
import xmu.vis.vo.ResNode;
import xmu.vis.vo.ResNodeType;
import xmu.vis.vo.ResRelation;

import xmu.vis.controller.VO.*;

import javax.xml.soap.Node;
import javax.xml.transform.Result;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.swing.UIManager.put;

@Service
public class VisService {

    @Autowired
    private RelationCheckMapper relationCheckMapper;

    @Autowired
    private AttributeMapper attributeMapper;

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

    // 删除待审核节点
    public Integer deleteUncheckedRelation(RelationCheck relationCheck){
        return relationCheckMapper.deleteUncheckedRelation(relationCheck);
    }

    // 插入待审核节点
    public Integer insertUncheckedRelation(RelationCheck relationCheck){
        return relationCheckMapper.insertUncheckedRelation(relationCheck);
    }

    // 返回所有待审核的节点
    public List<RelationCheck> getAllUncheckedRelation(){
        return relationCheckMapper.getAllUncheckedRelation();
    }

    //判断 关系三元组 是否存在
    public Boolean checkRelationExist(RelationTuple requestRelationTuple){
        RelationTuple checkRelation = relationTupleMapper.checkRelationTupleexist(requestRelationTuple);
        return checkRelation != null;
        // 存在返回true 不存在返回false
    }

    //判断 节点 是否存在(By Id)
    public Boolean checkNodeInfoExist(String requestNodeId){
        NodeInfo checkNodeInfo = nodeInfoMapper.getANodeInfoById(requestNodeId);
        return checkNodeInfo != null;
        // 存在返回true 不存在返回false
    }



/*
    public static HashMap<String, Object> nameToLabel = new HashMap<String, Object>() {
        {
            put("unitSequence", 0);
            put("biographicalInformation", 1);
            put("equipmentTree", 2);
        }
    };

    // 装备类型表 id pid name
    public List<HashMap> getEquipmentTypeLevelRelation() {
        List<HashMap> results = new ArrayList<HashMap>();
        List<EquipmentType> allEquipmentTypes = equipmentTypeMapper.getAllEquipmentType();
        for (EquipmentType equipmentType : allEquipmentTypes) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", equipmentType.getEquipmentTypeId());
            map.put("pid", equipmentType.getPid());
            map.put("name", equipmentType.getEquipmentType());
            results.add(map);
        }
        return results;
    }

    // 装备树表 id pid name
    public List<HashMap> getEquipmentTreeLevelRelation() {
        List<HashMap> results = new ArrayList<HashMap>();
        List<EquipmentTree> allEquipmentTrees = equipmentTreeMapper.getAllEquipmentTree();
        for (EquipmentTree equipmentTree : allEquipmentTrees) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", equipmentTree.getEquipmentId());
            map.put("pid", equipmentTree.getEquipmentTypeId());
            map.put("name", equipmentTree.getEquipmentName());
            results.add(map);
        }
        return results;
    }

    // 单位序列 id pid name
    public List<HashMap> getUnitSequenceLevelRelation() {
        List<HashMap> results = new ArrayList<HashMap>();
        List<UnitSequence> allUnitSequences = unitSequenceMapper.getAllUnitSequence();
        for (UnitSequence unitSequence : allUnitSequences) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", unitSequence.getUnitId());
            map.put("pid", unitSequence.getPid());
            map.put("name", unitSequence.getUnitFullName());
            results.add(map);
        }
        return results;
    }

    // 获取table中装备名称为name的装备的整个记录
    public List<HashMap> objectDataSwitch(String table, String name) {
        List<HashMap> result = new ArrayList<>();

        Object obj = null;
        if (table.equals("EquipmentType")) {
            obj = equipmentTypeMapper.getEquipmentTypeByName(name);
        } else if (table.equals("EquipmentTree")) {
            obj = equipmentTreeMapper.getEquipmentByName(name);
        } else if (table.equals("UnitSequence")) {
            obj = unitSequenceMapper.getUnitSequenceByName(name);
        } else {
            return result;
        }

        Class<?> objClass = obj.getClass();
        for (Field field : objClass.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                String attributeName = field.getName();
                Object content = field.get(obj);

                HashMap<String, Object> map = new HashMap<>();
                map.put("attributeName", attributeName);
                map.put("content", content);
                result.add(map);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    // 获取table的表头
    public List<String> objectAttribute(String table){
        List<String> result = new ArrayList<String>();
        Object obj = null;
        if(table.equals("EquipmentType")){
            obj = new EquipmentType();
        } else if(table.equals("EquipmentTree")){
            obj = new EquipmentTree();
        } else if(table.equals("UnitSequence")){
            obj = new UnitSequence();
        } else{
            return result;
        }

        Class<?> objClass = obj.getClass();
        for (Field field : objClass.getDeclaredFields()){
            field.setAccessible(true);
            String attributeName = field.getName();
            result.add(transAttr(attributeName));
        }
        return result;
    }

    // 获取单位序列（unitSequence)的父节点
    public HashMap<String, Object> getUnitSequenceRelation(UnitSequence unitSequence) {
        String unitName = unitSequence.getUnitFullName();
        String pid = unitSequence.getPid();

        UnitSequence fatherUnitSequence = unitSequenceMapper.getUnitSequenceById(pid);
        String fatherUnitName = fatherUnitSequence.getUnitFullName();

        HashMap<String, Object> map = new HashMap<>();
        map.put("fatherNode", fatherUnitName);
        map.put("childNode", unitName);
        map.put("nodeRelationType", "上一级单位");

        return map;
    }

    // 获取单位及其父单位的全称和单位序列对应的类型Label
    public List<HashMap> getUnitSequenceLabel(UnitSequence unitSequence) {
        List<HashMap> result = new ArrayList<>();

        String unitName = unitSequence.getUnitFullName();
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("nodeName", unitName);
        map1.put("label", nameToLabel.get("unitSequence"));
        result.add(map1);

        String pid = unitSequence.getPid();
        UnitSequence fatherUnitSequence = unitSequenceMapper.getUnitSequenceById(pid);
        String fatherUnitName = fatherUnitSequence.getUnitFullName();

        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("nodeName", fatherUnitName);
        map2.put("label", nameToLabel.get("unitSequence"));
        result.add(map2);

        return result;
    }

    // 获取人物资料表中人员及其所属单位
    public HashMap<String, Object> getBiographicalInformationRelation(BiographicalInformation biographicalInformation) {
        String personName = biographicalInformation.getPersonName();
        PersonnelInformation personnelInformation = personnelInformationMapper.getPersonnelInformationByPersonName(personName);
        String unitName = personnelInformation.getUnitName();

        HashMap<String, Object> map = new HashMap<>();
        map.put("fatherNode", unitName);
        map.put("childNode", personName);
        map.put("nodeRelationType", "所属人员");

        return map;
    }

    // 人名及人物资料表对应Label， 所属单位及单位序列对应的类型Label
    public List<HashMap> getBiographicalInformationLabel(BiographicalInformation biographicalInformation) {
        List<HashMap> result = new ArrayList<>();

        String personName = biographicalInformation.getPersonName();
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("nodeName", personName);
        map1.put("label", nameToLabel.get("biographicalInformation"));
        result.add(map1);

        PersonnelInformation personnelInformation = personnelInformationMapper.getPersonnelInformationByPersonName(personName);
        String unitName = personnelInformation.getUnitName();

        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("nodeName", unitName);
        map2.put("label", nameToLabel.get("unitSequence"));
        result.add(map2);

        return result;
    }

    // 装备名称和所属单位名称（父子节点关系）
    public HashMap<String, Object> getEquipmentRelation(EquipmentTree equipmentTree) {
        String equipmentName = equipmentTree.getEquipmentName();

        EquipmentAllocation equipmentAllocation = equipmentAllocationMapper.getEquipmentAllocationByEquipmentName(equipmentName);
        String unitName = equipmentAllocation.getUnitName();

        HashMap<String, Object> map = new HashMap<>();
        map.put("fatherNode", unitName);
        map.put("childNode", equipmentName);
        map.put("nodeRelationType", "配备装备");

        return map;
    }

    // 装备名称和所属单位名称（父子节点关系）及对应Label
    public List<HashMap> getEquipmentLabel(EquipmentTree equipmentTree){

        List<HashMap> result = new ArrayList<HashMap>();

        String equipmentName = equipmentTree.getEquipmentName();
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("nodeName", equipmentName);
        map1.put("label", nameToLabel.get("equipmentTree"));
        result.add(map1);

        EquipmentAllocation equipmentAllocation = equipmentAllocationMapper.getEquipmentAllocationByEquipmentName(equipmentName);
        String unitName = equipmentAllocation.getUnitName();

        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("nodeName", unitName);
        map2.put("label", nameToLabel.get("unitSequence"));
        result.add(map2);

        return result;
    }

    // 节点Name查询所属类型 装备序列/装备树/人物资料
    public String judgeNodeName(String nodeName){
        UnitSequence unitSequence = unitSequenceMapper.getUnitSequenceByName(nodeName);
        EquipmentTree equipmentTree = equipmentTreeMapper.getEquipmentByName(nodeName);
        BiographicalInformation biographicalInformation = (BiographicalInformation) biographicalInformationMapper.getBiographicalInformationByName(nodeName);

        if(unitSequence != null){
            return "unitSequence";
        } else if (equipmentTree != null){
            return "equipmentTree";
        } else if(biographicalInformation != null){
            return "biographicalInformation" ;
        } else{
            return "";
        }

    }

    // 从装备名称(nodename1) 到单位名 (nodename2)
    // （装备名称/单位， 上级单位， 关系）的序列（如果存在）
    public List<HashMap> getRelationBetweenEquipmentAndUnit(String nodeName1, String nodeName2){
        List<HashMap> result = new ArrayList<HashMap>();
        String unitName = equipmentAllocationMapper.getEquipmentAllocationByEquipmentName(nodeName1).getUnitName();
        UnitSequence unitSequence = unitSequenceMapper.getUnitSequenceByName(unitName);
        String pid = unitSequence.getPid();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("fatherNode", unitName);
        map.put("childNode", nodeName1);
        map.put("nodeRelationType", "配备装备");
        result.add(map);

        while((!pid.equals("0")) && (!unitName.equals(nodeName2))){
            UnitSequence fatherUnitSequence = unitSequenceMapper.getUnitSequenceById(pid);
            String fatherName = fatherUnitSequence.getUnitFullName();

            HashMap<String, Object> map1 = new HashMap<String, Object>();
            map1.put("fatherNode", fatherName);
            map1.put("childNode", unitName);
            map1.put("nodeRelationType", "上一级单位");
            result.add(map1);

            pid = fatherUnitSequence.getPid();
            unitName = fatherName;
        }
        if(!unitName.equals(nodeName2)){
            result.clear();
        }
        return result;
    }

    // 从装备名称(nodeName1)到单位名（nodeName2）的
    // （装备名称/单位，Label）的序列（如果存在）
    public List<HashMap> getLabelBetweenEquipmentAndUnit(String nodeName1, String nodeName2){
        List<HashMap> result = new ArrayList<HashMap>();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("nodeName", nodeName1);
        map.put("label", nameToLabel.get("equipmentTree"));
        result.add(map);

        String unitName = equipmentAllocationMapper.getEquipmentAllocationByEquipmentName(nodeName1).getUnitName();
        String pid = unitSequenceMapper.getUnitSequenceByName(unitName).getPid();

        while((!pid.equals("0")) && (!unitName.equals(nodeName2))){
            HashMap<String, Object> map1 = new HashMap<String , Object>();
            map1.put("nodeName", unitName);
            map1.put("label", nameToLabel.get("unitSequence"));
            result.add(map1);

            UnitSequence fatherUnitSequence = unitSequenceMapper.getUnitSequenceById(pid);
            pid = fatherUnitSequence.getPid();
            unitName = fatherUnitSequence.getUnitFullName();
        }
        if(!unitName.equals(nodeName2)){
            result.clear();
        } else{
            HashMap<String, Object> map2 = new HashMap<String, Object>();
            map2.put("nodeName", unitName);
            map2.put("label", nameToLabel.get("unitSequence"));
            result.add(map2);
        }
        return result;
    }

    // 从人员名到单位名（查询接口）返回的所有关系
    public List<HashMap> getRelationBetweenBiographicalAndUnit(String nodeName1, String nodeName2){
        List<HashMap> result = new ArrayList<HashMap>();
        String unitName = personnelInformationMapper.getPersonnelInformationByPersonName(nodeName1).getUnitName();
        UnitSequence unitSequence = unitSequenceMapper.getUnitSequenceByName(unitName);
        String pid = unitSequence.getPid();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("fatherNode", unitName);
        map.put("childNode", nodeName1);
        map.put("nodeRelationType", "所在单位");
        result.add(map);

        while((!pid.equals("0")) && (!unitName.equals(nodeName2))){
            UnitSequence fatherUnitSequence = unitSequenceMapper.getUnitSequenceById(pid);
            String fatherName = fatherUnitSequence.getUnitFullName();

            HashMap<String, Object> map1 = new HashMap<String, Object>();
            map1.put("fatherNode", fatherName);
            map1.put("childNode", unitName);
            map1.put("nodeRelationType", "上一级单位");
            result.add(map1);

            pid = fatherUnitSequence.getPid();
            unitName = fatherName;
        }
        if(!unitName.equals(nodeName2)){
            result.clear();
        }
        return result;
    }

    // 从人员名到单位名（查询接口）返回的所有Label
    public List<HashMap> getLabelBetweenBiographicalAndUnit(String nodeName1, String nodeName2){
        List<HashMap> result = new ArrayList<HashMap>();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("nodeName", nodeName1);
        map.put("label", nameToLabel.get("biographicalInformation"));
        result.add(map);

        String unitName = personnelInformationMapper.getPersonnelInformationByPersonName(nodeName1).getUnitName();
        String pid = unitSequenceMapper.getUnitSequenceByName(unitName).getPid();

        while((!pid.equals("0")) && (!unitName.equals(nodeName2))){
            HashMap<String, Object> map1 = new HashMap<String , Object>();
            map1.put("nodeName", unitName);
            map1.put("label", nameToLabel.get("unitSequence"));
            result.add(map1);

            UnitSequence fatherUnitSequence = unitSequenceMapper.getUnitSequenceById(pid);
            pid = fatherUnitSequence.getPid();
            unitName = fatherUnitSequence.getUnitFullName();
        }
        if(!unitName.equals(nodeName2)){
            result.clear();
        } else{
            HashMap<String, Object> map2 = new HashMap<String, Object>();
            map2.put("nodeName", unitName);
            map2.put("label", nameToLabel.get("unitSequence"));
            result.add(map2);
        }
        return result;
    }

    // 两个单位之间的联系路径（查询返回两个单位之间的所有Relation）
    public List<HashMap> getRelationBetweenUnitAndUnit(String nodeName1, String nodeName2){
        List<HashMap> result = new ArrayList<HashMap>();

        String unitId1 = unitSequenceMapper.getUnitSequenceByName(nodeName1).getUnitId();
        String unitId2 = unitSequenceMapper.getUnitSequenceByName(nodeName2).getUnitId();
        // union1应当是union2上级
        if (unitId1.length() < unitId2.length()){
            String temp = nodeName1;
            nodeName1 = nodeName2;
            nodeName2 = temp;
        }
        String unitName = nodeName1;
        String pid = unitSequenceMapper.getUnitSequenceByName(unitName).getPid();

        while((!pid.equals("0")) && (!unitName.equals(nodeName2))){
            UnitSequence fatherUnitSequence = unitSequenceMapper.getUnitSequenceById(pid);
            String fatherName = fatherUnitSequence.getUnitFullName();

            HashMap<String, Object> map1 = new HashMap<String, Object>();
            map1.put("fatherNode", fatherName);
            map1.put("childNode", unitName);
            map1.put("nodeRelationType", "上一级单位");
            result.add(map1);

            pid = fatherUnitSequence.getPid();
            unitName = fatherName;
        }
        if(!unitName.equals(nodeName2)){
            result.clear();
        }
        return result;
    }

    // 两个单位之间的联系，查询返回两个单位之间的所有Label
    public List<HashMap> getLabelBetweenUnitAndUnit(String nodeName1, String nodeName2){
        List<HashMap> result = new ArrayList<HashMap>();

        String unitId1 = unitSequenceMapper.getUnitSequenceByName(nodeName1).getUnitId();
        String unitId2 = unitSequenceMapper.getUnitSequenceByName(nodeName2).getUnitId();
        // union1应当是union2上级
        if (unitId1.length() < unitId2.length()){
            String temp = nodeName1;
            nodeName1 = nodeName2;
            nodeName2 = temp;
        }
        String unitName = nodeName1;
        String pid = unitSequenceMapper.getUnitSequenceByName(unitName).getPid();

        while((!pid.equals("0")) && (!unitName.equals(nodeName2))){
            HashMap<String, Object> map1 = new HashMap<String , Object>();
            map1.put("nodeName", unitName);
            map1.put("label", nameToLabel.get("unitSequence"));
            result.add(map1);

            UnitSequence fatherUnitSequence = unitSequenceMapper.getUnitSequenceById(pid);
            pid = fatherUnitSequence.getPid();
            unitName = fatherUnitSequence.getUnitFullName();
        }
        if(!unitName.equals(nodeName2)){
            result.clear();
        } else{
            HashMap<String, Object> map2 = new HashMap<String, Object>();
            map2.put("nodeName", unitName);
            map2.put("label", nameToLabel.get("unitSequence"));
            result.add(map2);
        }
        return result;

    }

    public List<HashMap> getAllNodesRelationship(){
        List<HashMap> result = new ArrayList<>();

        // 单位关系
        List<UnitSequence> seqs = unitSequenceMapper.getAllUnitSequence();
        for(UnitSequence seq: seqs){
            String UnionName = seq.getUnitFullName();
            String Pid = seq.getPid();
            // 可优化
            String fatherName = unitSequenceMapper.getUnitSequenceById(Pid).getUnitFullName();
            result.add(new HashMap<String, Object>(){{
                put("fatherNode",fatherName);
                put("childNode", UnionName);
                put("nodeRelationType", "上一级单位");
            }});
        }

        // 人员
        List<PersonnelInformation> personals = personnelInformationMapper.getAllPersonnelInformation();
        for(PersonnelInformation personal: personals){
            String PersonalName = personal.getPersonName();
            if(PersonalName.length()==0) {continue;}
            String fatherName = personal.getUnitName();
            result.add(new HashMap<String , Object>(){{
                put("fatherNode", fatherName);
                put("childNode", PersonalName);
                put("nodeRelationType", "所在单位");
            }});
        }

        // 武器装备
        List<EquipmentAllocation> eqms = equipmentAllocationMapper.getAllEquipmentAllocation();
        for(EquipmentAllocation eqm: eqms){
            String EqmName = eqm.getEquipmentName();
            String fatherName = eqm.getUnitName();
            result.add(new HashMap<String , Object>(){{
                put("fatherNode", fatherName);
                put("childNode", EqmName);
                put("nodeRelationType", "配备装备");
            }});
        }
        return  result;
    }

    public List<HashMap> getAllNodesWithLabel(){
        List<HashMap> result = new ArrayList<>();

        // 单位关系
        List<UnitSequence> seqs = unitSequenceMapper.getAllUnitSequence();
        for(UnitSequence seq: seqs){
            String UnionName = seq.getUnitFullName();
            result.add(new HashMap<String, Object>(){{
                put("nodeName", UnionName);
                put("label", nameToLabel.get("unitSequence"));
            }});
        }

        // 人员
        List<PersonnelInformation> personals = personnelInformationMapper.getAllPersonnelInformation();
        for(PersonnelInformation personal: personals){
            String PersonalName = personal.getPersonName();
            if(PersonalName.length()==0){
                continue;
            }
            result.add(new HashMap<String, Object>(){{
                put("nodeName", PersonalName);
                put("label", nameToLabel.get("BiographicalInformation"));
            }});
        }

        // 武器装备
        List<EquipmentAllocation> eqms = equipmentAllocationMapper.getAllEquipmentAllocation();
        for(EquipmentAllocation eqm : eqms){
            String EqmName = eqm.getEquipmentName();
            result.add(new HashMap<String, Object>(){{
                put("nodeName", EqmName);
                put("label", nameToLabel.get("equipmentTree"));
            }});
        }

        return result;
    }

    public List<String> getChildrenNodeNameByFatherNode(String fatherNodeName, String relationTypeName){
        NodeInfo nodeInfo = nodeInfoMapper.getNodeInfoByName(fatherNodeName);
        if (nodeInfo == null){
            return new ArrayList<String>();
        }
        Object relationTypeId = relationTypeMapper.getRelationTypeIdByRelationTypeName(relationTypeName);
        if(relationTypeId == null){
            return new ArrayList<String>();
        }
        List<Integer> childrenNodeIds = nodeRelationInfoMapper.getNode1ByNode2AndRelation(nodeInfo.getNodeInfoId(), (int) relationTypeId);
        if(childrenNodeIds.isEmpty()){
            return new ArrayList<String>();
        }

        List<String> childrenNames = new ArrayList<String>();
        for (int i = 0; i < childrenNodeIds.size(); i++){
            int childNodeId = childrenNodeIds.get(i);
            String childName = nodeInfoMapper.getNodeNameById(childNodeId).get("name").toString();
            childrenNames.add(childName);
        }
        return childrenNames;
    }
    // 英文转中文
    private String transAttr(String attributeName){
        String res = new String("");
        switch (attributeName){
            case "unitId":
                res = "单位ID";
                break;
            case "unitFullName":
                res = "单位全称";
                break;
            case "unitName":
                res = "单位名称";
                break;
            case "attribute":
                res = "属性";
                break;
            case "nature":
                res = "性质";
                break;
            case "services":
                res = "军种";
                break;
            case "arms":
                res = "兵种";
                break;
            case "categoryId":
                res = "类别ID";
                break;
            case "category":
                res = "类别";
                break;
            case "commandRelationship":
                res = "指挥关系";
                break;
            case "warZone":
                res = "战区";
                break;
            case "campId":
                res = "营区ID";
                break;
            case "camp":
                res = "营区";
                break;
            case "task":
                res = "任务";
                break;
            case "concreteTasks":
                res = "具体任务";
                break;
            case "establishment":
                res = "编制";
                break;
            case "peacetimeGarrison":
                res = "平地驻地";
                break;
            case "peacetimeGarrisonLongitude":
                res = "平时驻地经度";
                break;
            case "peacetimeGarrisonLatitude":
                res = "平时驻地纬度";
                break;
            case "wartimeGarrison":
                res = "战时驻地";
                break;
            case "wartimeGarrisonLongtitude":
                res = "战时驻地经度";
                break;
            case "wartimeGarrisonLatitude":
                res = "战时驻地纬度";
                break;
            case "establishmentDate":
                res = "成立日期";
                break;
            case "abolitionDate":
                res = "裁撤日期";
                break;
            case "detailedInfo":
                res = "详细情况";
                break;
            case "remarks1":
                res = "备注1";
                break;
            case "remarks2":
                res = "备注2";
                break;
            case "equipmentTypeId":
                res = "装备类型ID";
                break;
            case "equipmentType":
                res = "装备类型";
                break;
            case "equipmentId":
                res = "装备ID";
                break;
            case "equipmentName":
                res = "装备名称";
                break;
            case "equipmentMajorType":
                res = "装备大类";
                break;
            case "equipmentPurpose":
                res = "装备用途";
                break;
            case "baseInfo":
                res = "基本情况";
                break;
            case "structureFeature":
                res = "结构特征";
                break;
            case "systemComposition":
                res = "系统组成";
                break;
            case "deployment":
                res = "部署运用";
                break;
            case "development":
                res = "未来发展";
                break;
            case "combatTechnicalPerformance":
                res = "战技性能";
                break;
            case "basicParams":
                res = "基本参数";
                break;
            case "dynamicPerformance":
                res = "动力性能";
                break;
            case "specialPerformance":
                res = "特殊性能";
                break;
            case "equippedWeapons":
                res = "配备武器";
                break;
            case "commandAndControlSystem":
                res = "指控系统";
                break;
            case "reconnaissanceEquipment":
                res = "侦搜装备";
                break;
            case "ewEquipment":
                res = "电子战装备";
                break;
            case "communicationEquipment":
                res = "通信装备";
                break;
            case "iffEquipment":
                res = "敌我识别装备";
                break;
            case "defenseSystem":
                res = "防护系统";
                break;
            case "extensionName":
                res = "拓展名";
                break;
            default:
                res = attributeName;
                break;
        }
        return res;
    }

 */
}
