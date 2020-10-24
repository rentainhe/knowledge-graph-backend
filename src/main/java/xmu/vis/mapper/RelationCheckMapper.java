package xmu.vis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import xmu.vis.domain.RelationCheck;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface RelationCheckMapper {

    public List<RelationCheck> getAllUncheckedRelation(); // 获取所有待审核节点

    public Integer insertUncheckedRelation(RelationCheck relationCheck);

    public Integer deleteUncheckedRelation(RelationCheck relationCheck);

}
