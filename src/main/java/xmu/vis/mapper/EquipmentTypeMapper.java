package xmu.vis.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import xmu.vis.domain.EquipmentType;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface EquipmentTypeMapper {


    public EquipmentType getRootNode();
    public List<EquipmentType> getAllEquipmentType();

    public EquipmentType getEquipmentTypeById(String equipmentTypeId);
    public EquipmentType getEquipmentTypeByName(String equipmentTypeName);

    public Integer insertEquipmentType(EquipmentType equipmentType);
    public Integer deleteEquipmentType(String equipmentTypeName);

    public Integer insertAttributeName(String attributeName);
    public Integer deleteAttributeName(String attributeName);

    public Integer modifyAttributeName(String oldAttributeName, String newAttributeName);
    public Integer modifyAttributeValue(String equipmentTypeName, Map<String, Object> map);

    public void testEquipmentTypeMapper();

}
