package xmu.vis.domain;

import lombok.Data;

import java.io.Serializable;

@Data

// -------------  装备配赋  ---------------
public class EquipmentAllocation implements Serializable {

    private String id;

    private String unitType;
    private String unitName;
    private String unitId;

    private String equipmentType;
    private String equipmentName;
    private String equipmentId;

    private String establishmentQuantity;
    private String actualQuantity;
    private String remark;
}
