package xmu.vis.service;
/*
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xmu.vis.domain.NodeInfo;
import xmu.vis.domain.NodeRelationInfo;
import xmu.vis.domain.NodeRelationInfoHistory;
import xmu.vis.domain.TextInfo;
import xmu.vis.mapper.NodeInfoMapper;
import xmu.vis.mapper.NodeRelationInfoHistoryMapper;
import xmu.vis.mapper.NodeRelationInfoMapper;
import xmu.vis.mapper.TextInfoMapper;

import java.util.List;

@Service
public class TextInfoService {

    @Autowired
    private TextInfoMapper textInfoMapper;

    @Autowired
    private NodeInfoMapper nodeInfoMapper;

    @Autowired
    private NodeRelationInfoMapper nodeRelationInfoMapper;

    @Autowired
    private NodeRelationInfoHistoryMapper nodeRelationInfoHistoryMapper;

    public Integer insertText(TextInfo textInfo) {
        return textInfoMapper.insertText(textInfo);
    }

    public TextInfo getTextInfoById(Integer id) {
        return textInfoMapper.getTextInfoById(id);
    }

    public Integer updateTextInfo(TextInfo textInfo) {
        return textInfoMapper.updateTextInfo(textInfo);
    }

    public Integer updateTextStatus(TextInfo textInfo) {
        return textInfoMapper.updateTextStatus(textInfo.getId(), textInfo.getStatus());
    }

    public List<TextInfo> getAllText() {
        return textInfoMapper.getAllText();
    }

    public Integer updateGraph(String nodeName1, String nodeName2) {
        NodeInfo node1 = nodeInfoMapper.getNodeInfoByName(nodeName1);
        NodeInfo node2 = nodeInfoMapper.getNodeInfoByName(nodeName2);
        if (node1 == null || node2 == null) {
            return 0;
        }
        // 保存历史记录
        NodeRelationInfo oldRelation = nodeRelationInfoMapper.getNodeRelationByNode1(node1.getNodeInfoId());
        NodeRelationInfoHistory his = new NodeRelationInfoHistory(oldRelation);
        nodeRelationInfoHistoryMapper.insertHistory(his);

        // 修改node1里的球队名属性
//        JSONObject jsonObject = JSON.parseObject(node1.getAttribute());
//        jsonObject.put("球队名",nodeName2);
//        node1.setAttribute(jsonObject.toString());
//        nodeInfoMapper.updateAttribute(node1);

        // 修改联系
        nodeRelationInfoMapper.updateRelation(oldRelation.getId(), node2.getNodeInfoId());
        return 1;
    }
}
*/