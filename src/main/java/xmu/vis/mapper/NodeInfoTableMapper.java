package xmu.vis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import xmu.vis.domain.NodeInfoTable;

@Mapper
@Component
public interface NodeInfoTableMapper {
    public NodeInfoTable getNodeInfoByid(String nodeinfoid);
}
