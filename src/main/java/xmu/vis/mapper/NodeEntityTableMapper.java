package xmu.vis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import xmu.vis.domain.NodeEntityTable;

import java.util.HashMap;
import java.util.List;

@Mapper
@Component
public interface NodeEntityTableMapper {

    public Integer addNewNodeEntity(NodeEntityTable newnodeEntity);

    public List<NodeEntityTable> initGraph();

    public NodeEntityTable getNodeEntityByNodeKey(String nodeEntityKey);
}
