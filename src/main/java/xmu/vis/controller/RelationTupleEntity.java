package xmu.vis.controller;

import lombok.Data;

@Data
public class RelationTupleEntity {
    public String fatherNodeTypeName;
    public String fatherNodeEntityAttribute;

    public String childNodeTypeName;
    public String childNodeEntityAttribute;

    public String relationTypeName;
    public String relationTypeAttribute;

}
