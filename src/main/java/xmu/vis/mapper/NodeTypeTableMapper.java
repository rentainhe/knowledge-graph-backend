package xmu.vis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import xmu.vis.domain.NodeTypeTable;

@Mapper
@Component
public interface NodeTypeTableMapper {
    public NodeTypeTable test();

}
