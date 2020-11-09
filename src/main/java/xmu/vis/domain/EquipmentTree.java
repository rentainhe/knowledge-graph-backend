package xmu.vis.domain;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data

// ------------  装备树  -----------
public class EquipmentTree implements Serializable {

    private String equipmentId;    //装备ID
    private String equipmentName;    //装备名称
    private String equipmentTypeId;     //装备类型ID
    private String equipmentMajorType;    //装备大类
    private String equipmentType;     //装备类型
    private String equipmentPurpose;    //装备用途

    private String baseInfo;      //基本情况
    private String structureFeature;   //结构特征
    private String systemComposition;    //系统组成

    private String deployment;    //部署运用
    private String development;     //未来发展
    private String combatTechnicalPerformance;   //战技性能

    private String basicParams;   //基本参数
    private String dynamicPerformance;  //动力性能
    private String specialPerformance;   //特殊性能

    private String equippedWeapons;   //配备武器
    private String commandAndControlSystem;   //指挥系统
    private String reconnaissanceEquipment;     //侦搜装备

    private String ewEquipment;         //电子战装备
    private String communicationEquipment;      //通信装备
    private String iffEquipment;    //敌我识别装备
    private String defenseSystem;       //防护系统
    private String extensionName;       //扩展名
}
