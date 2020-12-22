package xmu.vis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import xmu.vis.domain.NodeTypeTable;
import xmu.vis.domain.RelationTupleTable;

import java.util.HashMap;
import java.util.List;

@Mapper
@Component
public interface RelationTupleTableMapper {
    public Integer addNewRelationTuple(RelationTupleTable newRelationTuple);
}
