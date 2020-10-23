package xmu.vis.domain;

import lombok.Data;

import java.io.Serializable;

@Data

//  -----------   装备类型   -------------
public class EquipmentType implements Serializable {

    private String equipmentTypeId;

    private String pid;

    private String equipmentType;
}
