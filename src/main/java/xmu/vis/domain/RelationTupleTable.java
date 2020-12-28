package xmu.vis.domain;

import lombok.Data;
import java.io.Serializable;

@Data
public class RelationTupleTable implements Serializable{
    private String relationKey; // 唯一key
    private String fatherNodeKey;// 唯一key
    private String childNodeKey;// 唯一key
    private String relationTypeName;
    private String relationAttribute;
}
