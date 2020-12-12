package xmu.vis.domain;

import lombok.Data;
import java.io.Serializable;

@Data
public class RelationTypeTable implements Serializable {
    private String relationtypeid;//关系类型id
    private String relationname;//关系类型名
    private String relationattribute;//该类型关系属性
}
