package xmu.vis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import xmu.vis.domain.EquipmentAllocation;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface EquipmentAllocationMapper {

    public List<EquipmentAllocation> getAllEquipmentAllocation();

    public EquipmentAllocation getEquipmentAllocationById(String requestId);//唯一确定的标识符

    public List<EquipmentAllocation> getEquipmentAllocationByUnitId(String unitId);

    public List<EquipmentAllocation> getEquipmentAllocationByUnitName(String unitName);

    public List<EquipmentAllocation> getEquipmentAllocationByEquipmentName(String equipmentName);

    public List<EquipmentAllocation> getEquipmentAllocationByEquipmentId(String equipmentId);//一个武器ID对应一个武器 但这个武器可能不止装备给一个单位

    public Integer deleteEquipmentAllocationByUnitIdEquipmentId(String unitId, String equipmentId);
    public Integer modifyAttributeValue(String requestId, Map<String, String> map);
}
