package xmu.vis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import xmu.vis.domain.NodeTypeTable;

import java.util.HashMap;
import java.util.List;

@Mapper
@Component
public interface NodeTypeTableMapper {
    public Integer addNewNodeType(NodeTypeTable newNodeType);//新建一个NodeType

    public List<NodeTypeTable> getAllNodeTypeNameAndAttribute();//返回所有Nodetype以及对应属性

    public String getNodeAttributeDictByNodeTypeId(String nodeTypeId);//获得某类型节点的属性

    public Integer deleteNodeType(String nodeTypeName); // 删除某类型节点

}
