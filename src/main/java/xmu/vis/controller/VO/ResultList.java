package xmu.vis.controller.VO;

import xmu.vis.domain.*;

import java.util.List;


// 主要根据name来查询,当name对应的数据不止一条时,返回所有名称为name的对象,保存在ResultList
public class ResultList {
    List<UnitSequence> returnUnitSequenceList;
    List<CharacterData> returnCharacterDataList;
    List<EquipmentTree> returnEquipmentTreeList;

    public List<UnitSequence> getUnitSequenceList(){
        return returnUnitSequenceList;
    }
    public List<CharacterData> getCharacterList(){
        return returnCharacterDataList;
    }
    public List<EquipmentTree> getEquipmentTree(){
        return returnEquipmentTreeList;
    }

    public void setUnitsequenceList(List<UnitSequence> unitSequenceList){
        this.returnUnitSequenceList = unitSequenceList;
    }
    public void setCharacterDataList(List<CharacterData> characterDataList){
        this.returnCharacterDataList = characterDataList;
    }
    public void setEquipmentList(List<EquipmentTree> equipmentTreeList){
        this.returnEquipmentTreeList = equipmentTreeList;
    }

    public void addUnitSequence(List<UnitSequence> unit){
        returnUnitSequenceList.addAll(unit);
    }

    public void addCharacterData(List<CharacterData> character){
        returnCharacterDataList.addAll(character);
    }
    public void addEquipmentTree(List<EquipmentTree> equipmentTree){
        returnEquipmentTreeList.addAll(equipmentTree);
    }

}



