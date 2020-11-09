package xmu.vis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import xmu.vis.domain.UnitSequence;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface UnitSequenceMapper {

    public List<UnitSequence> getAllUnitSequence();

    public UnitSequence getUnitSequenceById(String unitId);
    public UnitSequence getUnitSequenceByFullName(String unitName);
    public List<UnitSequence> getUnitSequenceByName(String unitName);

    public Integer insertUnitSequence(UnitSequence unitSequence);
    public Integer deleteUnitSequence(String unitFullName); //通过单位全称来删除节点

    public Integer insertAttributeName(String attributeName);
    public Integer deleteAttributeName(String attributeName);

    public Integer modifyAttributeName(String oldAttributeName, String newAttributeName);
    public Integer modifyAttributeValue(String unitId, Map<String, String> map);

    public List<String> showAllColumns();
    //public Integer changeUnitAttirbuteById(String unitID, UnitSequence newUnitAttribute);
    public void testUnitSequenceMapper();

}
