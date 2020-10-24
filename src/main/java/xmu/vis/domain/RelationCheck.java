package xmu.vis.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class RelationCheck {

    private Integer unCheckedId;   // 待审核关系的ID

    private String startNodeName;   // 起始节点名称

    private String startNodeType; // 起始节点类型

    private String endNodeName; // 终止节点名称

    private String endNodeType; // 终止节点类型

    private String relation;   // 节点间关系

}
