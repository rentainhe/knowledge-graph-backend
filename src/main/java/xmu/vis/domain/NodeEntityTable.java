package xmu.vis.domain;

import lombok.Data;
import java.io.Serializable;

@Data
public class NodeEntityTable implements Serializable{
    private String nodeEntityKey;//实体节点的name(或者说是唯一key
    private String nodeEntityTypeName;//实体节点的类型
    private String nodeEntityAttribute;//实体节点属性
}
