package xmu.vis.domain;

import lombok.Data;
import java.io.Serializable;

@Data
public class NodeInfoTable implements Serializable{

    private String nodeinfoid;//实体节点的id

    private String nodetypeid;//实体节点的类型id

    private String nodeinfoattribute;//实体节点属性
}
