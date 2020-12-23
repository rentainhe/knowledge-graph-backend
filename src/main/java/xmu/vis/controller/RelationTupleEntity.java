package xmu.vis.controller;

import lombok.Data;

@Data
public class RelationTupleEntity {
    public TableKeywords fathernode;
    public TableKeywords childnode;

    public String relationTypeName;
    public String relationTypeAttribute;

}
