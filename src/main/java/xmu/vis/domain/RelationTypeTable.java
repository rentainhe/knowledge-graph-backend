package xmu.vis.domain;

import lombok.Data;
import java.io.Serializable;

@Data
public class RelationTypeTable implements Serializable {
    private String relationtypeid;//关系类型id
    private String relationtypename;//关系类型名
    private String relationtypeattribute;//该类型关系属性
}
