package xmu.vis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import xmu.vis.domain.NodeTypeTable;

import java.util.HashMap;

@Mapper
@Component
public interface NodeTypeTableMapper {
    public Integer addNewNodeType(NodeTypeTable newNodeType);

    public String getNodeAttributeDictByNodeTypeId(String nodeTypeId);//获得某类型节点的属性

}
