package xmu.vis.domain;

import lombok.Data;

import java.io.Serializable;

@Data
// ---------   人事信息   ----------
public class PersonnelInformation implements Serializable {

    private String unitId;   //单位ID
    private String unitName;   //单位名称

    private String personId;   //人员ID
    private String personName;   //人员姓名

    private String militaryPost;    // 职务
    private String militaryRank;    // 级职

    private String date;    //时间
    private String predecessor;    //前任
}
