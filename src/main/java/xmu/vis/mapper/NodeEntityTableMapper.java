package xmu.vis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import xmu.vis.domain.NodeEntityTable;

import java.util.HashMap;

@Mapper
@Component
public interface NodeEntityTableMapper {
    public NodeEntityTable getNodeEntityAttributeByid(String nodeinfoid);

    public Integer updateNodeEntityAttributeById(String nodeId, HashMap<String, String> updatedAttribute);

//    public Integer updateNodeEntityAttributeById(@Param() String nodeId, HashMap<String, String> updatedAttribute);

}
