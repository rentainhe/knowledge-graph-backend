package xmu.vis.domain;

import lombok.Data;

import java.io.Serializable;

@Data
// 关系审核类
public class RelationCheck {

    private Integer UnCheckedId;   // 待审核关系的ID

    private String StartNodeName;   // 起始节点名称

    private String StartNodeType; // 起始节点类型

    private String EndNodeName; // 终止节点名称

    private String EndNodeType; // 终止节点类型

    private String Relation;   // 节点间关系

}
