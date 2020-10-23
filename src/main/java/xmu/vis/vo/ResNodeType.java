package xmu.vis.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResNodeType implements Serializable {

    public ResNodeType(Integer id, String name, Integer pid) {
        this.id = id;
        this.name = name;
        this.pid = pid;
    }

    private Integer id;

    private String name;

    private Integer pid;
}
