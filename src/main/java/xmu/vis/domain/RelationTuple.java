package xmu.vis.domain;

import lombok.Data;

import java.io.Serializable;

@Data
// -----------  关系三元组 -------------
public class RelationTuple implements Serializable{

    private String fatherId;   //父节点ID
    private String fatherName;
    private String childId;   //子节点ID
    private String childName;
    private String relationName;   //关系ID
}
