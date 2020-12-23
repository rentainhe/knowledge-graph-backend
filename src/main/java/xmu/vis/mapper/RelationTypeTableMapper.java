package xmu.vis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import xmu.vis.domain.NodeTypeTable;
import xmu.vis.domain.RelationTupleTable;
import xmu.vis.domain.RelationTypeTable;

import java.util.HashMap;
import java.util.List;

@Mapper
@Component
public interface RelationTypeTableMapper {
    public Integer addNewRelationType(RelationTypeTable relaitonType);
}
