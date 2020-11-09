package xmu.vis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import xmu.vis.domain.NodeInfo;

import java.util.List;
import java.util.Map;


@Mapper
@Component
public interface NodeInfoMapper {
    public List<NodeInfo> getAllNodeInfo();

    public NodeInfo getANodeInfoById(String theNodeId);
    public List<NodeInfo> getANodeInfoByName(String theNodeName);

    public Integer deleteANodeInfo(NodeInfo nodeInfo);

    public Integer insertANodeInfo(NodeInfo nodeInfo);

    public List<String> showAllColumns();
}
