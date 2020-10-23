package xmu.vis.domain;

import lombok.Data;

import java.io.Serializable;

@Data
// ----------  关系类型信息 ----------
public class RelationType implements Serializable {

    private String relationTypeId;

    private String relationTypeName;
}
