package xmu.vis.domain;

import lombok.Data;
import java.io.Serializable;

@Data
public class RelationTypeTable implements Serializable {
    private String relationTypeName;//关系类型名
    private String relationTypeAttribute;//该类型关系属性
}
