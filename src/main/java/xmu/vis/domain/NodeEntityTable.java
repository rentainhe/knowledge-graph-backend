package xmu.vis.domain;

import lombok.Data;
import java.io.Serializable;

@Data
public class NodeEntityTable implements Serializable{
    private String nodeentityid;//实体节点的id
    private String nodeentitytypeid;//实体节点的类型id
    private String nodeentityattribute;//实体节点属性
}
