package xmu.vis.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResRelation implements Serializable {

    // 起点
    private String source;

    // 终点
    private String target;

    // 线上填的值
    private String lineWord;

    public ResRelation(String source, String target, String lineWord) {
        this.source = source;
        this.target = target;
        this.lineWord = lineWord;
    }

}
