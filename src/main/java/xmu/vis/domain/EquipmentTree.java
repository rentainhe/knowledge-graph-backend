package xmu.vis.domain;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data

// ------------  装备树  -----------
public class EquipmentTree implements Serializable {

    private String equipmentId;
    private String equipmentName;
    private String equipmentTypeId;
    private String equipmentMajorType;
    private String equipmentType;
    private String equipmentPurpose;

    private String baseInfo;
    private String structureFeature;
    private String systemComposition;

    private String deployment;
    private String development;
    private String combatTechnicalPerformance;

    private String basicParams;
    private String dynamicPerformance;
    private String specialPerformance;

    private String equippedWeapons;
    private String commandAndControlSystem;
    private String reconnaissanceEquipment;

    private String ewEquipment;
    private String communicationEquipment;
    private String iffEquipment;
    private String defenseSystem;
    private String extensionName;
}
