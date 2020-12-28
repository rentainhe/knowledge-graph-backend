package xmu.vis.controller;

import lombok.Data;

@Data
public class RelationTupleEntity {
    public TableKeywords fathernode;
    public TableKeywords childnode;
    public TableKeywords relation;
}
