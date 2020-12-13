package xmu.vis.domain;

import lombok.Data;
import java.io.Serializable;

@Data
public class NodeTypeTable implements Serializable{
    private String nodetypeid;//节点类型id
    private String nodetypename;//节点类型名
    private String nodetypeattribute;//该类型节点属性
}
