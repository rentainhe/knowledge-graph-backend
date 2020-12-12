package xmu.vis.domain;

import lombok.Data;
import java.io.Serializable;

@Data
public class RelationTupleTable implements Serializable{
    private String fathernodeid;
    private String childnodeid;
    private String relationid;
}
