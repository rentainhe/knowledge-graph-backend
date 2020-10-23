package xmu.vis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import xmu.vis.domain.RelationType;
@Mapper
@Component
public interface RelationTypeMapper {
    public String getRelationNameByRelationId(String relationId);
}
