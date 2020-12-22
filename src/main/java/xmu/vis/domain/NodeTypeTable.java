package xmu.vis.domain;

import lombok.Data;
import java.io.Serializable;

@Data
public class NodeTypeTable implements Serializable{
    private String nodeTypeName;//节点类型名
    private String nodeTypeAttribute;//该类型节点属性
}
