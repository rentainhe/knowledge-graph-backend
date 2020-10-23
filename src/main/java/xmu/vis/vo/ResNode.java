package xmu.vis.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResNode implements Serializable {

    private Integer id;

    private String name;

    private int group;

    public ResNode(Integer id, String name, int group) {
        this.id = id;
        this.name = name;
        this.group = group;
    }
}
