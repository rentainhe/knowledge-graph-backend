package xmu.vis.domain;

import lombok.Data;

import java.io.Serializable;

@Data
// ---------   人物资料  -------------
public class CharacterData implements Serializable {

    private String personId;   //人员ID

    private String personName;  //人员姓名

    private String basicInfo;  //基本资料

    private String resume;   //履历

    private String otherInfo;   //其他
}