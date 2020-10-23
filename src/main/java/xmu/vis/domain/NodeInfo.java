package xmu.vis.domain;

import lombok.Data;

import java.io.Serializable;

@Data

//   -----------   节点信息  --------
public class NodeInfo implements Serializable {

    private String nodeId;   //节点ID

    private Integer label;   //节点类型   0:单位  1：人员  2：武器

    private String nodeName;   //节点名称
}
