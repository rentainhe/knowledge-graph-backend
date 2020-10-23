package xmu.vis.domain;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
// ------------  单位序列  ---------------
public class UnitSequence implements Serializable {

    private String unitId;

    private String pid;

    private String unitFullName;

    private String unitName;

    private String attribute;

    private String nature;

    private String services;

    private String arms;


    private String categoryId;

    private String category;

    private String commandRelationship;

    private String warZone;

    private String campId;

    private String camp;

    private String task;

    private String concreteTasks;

    private String establishment;   //  编制

    private String peacetimeGarrison;

    private String peacetimeGarrisonLongitude;

    private String peacetimeGarrisonLatitude;

    private String wartimeGarrison;

    private String wartimeGarrisonLongitude;

    private String wartimeGarrisonLatitude;

    private String establishmentDate;

    private String abolitionDate;

    private String baseInfo;

    private String detailedInfo;

    private String remarks1;

    private String remarks2;
}
