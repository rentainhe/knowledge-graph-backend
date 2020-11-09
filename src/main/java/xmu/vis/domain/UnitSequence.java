package xmu.vis.domain;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
// ------------  单位序列  ---------------
public class UnitSequence implements Serializable {

    private String unitId; //单位ID

    private String pid;  //PID

    private String unitFullName;   //单位全称

    private String unitName;   //单位名称

    private String attribute;   //属性

    private String nature;   //性质

    private String services;   //军种

    private String arms;    //兵种


    private String categoryId;   //类别ID

    private String category;   //类别

    private String commandRelationship;   //指挥关系

    private String warZone;  //战区

    private String campId;  //营区ID

    private String camp;  //营区

    private String task;  //任务

    private String concreteTasks;  //具体任务

    private String establishment;   //  编制

    private String peacetimeGarrison;  //平时驻地

    private String peacetimeGarrisonLongitude;//平时驻地经度

    private String peacetimeGarrisonLatitude;//平时驻地纬度

    private String wartimeGarrison;//战时驻地

    private String wartimeGarrisonLongitude;//战时驻地经度

    private String wartimeGarrisonLatitude;//战时驻地纬度

    private String establishmentDate;//成立日期

    private String abolitionDate;//裁撤日期

    private String baseInfo;//基本情况

    private String detailedInfo;//详细情况

    private String remarks1;//备注1

    private String remarks2;//备注2
}
