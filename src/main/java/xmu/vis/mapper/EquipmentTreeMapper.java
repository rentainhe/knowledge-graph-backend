package xmu.vis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import xmu.vis.domain.EquipmentTree;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface EquipmentTreeMapper {

//    public EquipmentTree getRootNode();
//    public List<EquipmentTree> getAllEquipmentTree();

    public EquipmentTree getEquipmentById(String equipmentId);
    public List<EquipmentTree> getEquipmentByName(String equipmentName);

    public Integer insertEquipmentTree(EquipmentTree equipmentTree);
    public Integer deleteEquipmentTree(EquipmentTree equipmentTree); //通过 装备ID与装备名称 定位

//    public Integer insertAttributeName(String attributeName);
//    public Integer deleteAttributeName(String attributeName);

    public Integer modifyAttributeName(String oldAttributeName, String newAttributeName);
    public Integer modifyAttributeValue(String equipmentId, Map<String, String> map);

    public List<String> showAllColumns();

    public void testEquipmentTreeMapper();

}
