package xmu.vis.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//import xmu.vis.domain.TextInfo;
//import xmu.vis.service.ModelService;
//import xmu.vis.service.TextInfoService;
//import xmu.vis.utils.ResponseUtil;
//
//import java.util.ArrayList;
//
//@RestController
//@CrossOrigin(origins = "*")
//public class TextInfoController {
//
//    @Autowired
//    private TextInfoService textInfoService;
//
//    @Autowired
//    private ModelService modelService;
//
//    /**
//     * 上传文本
//     * @param textInfo
//     * @return
//     */
//    @PostMapping("/uploadText")
//    public Object uploadText(@RequestBody TextInfo textInfo) {
//        System.out.println(textInfo);
//        if (textInfoService.insertText(textInfo) == 1) {
//            return ResponseUtil.ok(textInfo);
//        } else {
//            return ResponseUtil.fail();
//        }
//    }
//
//    /**
//     * 根据id获取展示的信息
//     * @param tid 上传文本返回的textInfo的id
//     * @return
//     */
//    @Transactional(rollbackFor = Exception.class)
//    @GetMapping("/showExtractInfo/{tid}")
//    public Object showExtractInfo(@PathVariable("tid") Integer tid) {
//        TextInfo textInfo = textInfoService.getTextInfoById(tid);
//        if (textInfo == null) {
//            return ResponseUtil.fail();
//        }
//        Object res = modelService.extractKnowledge(textInfo.getContent());
//        if (res == null) {
//            return ResponseUtil.fail();
//        } else {
//            ArrayList<String> ans = (ArrayList<String>) res;
//            textInfo.setExtractNode1(ans.get(0));
//            textInfo.setExtractRelation(ans.get(1));
//            textInfo.setExtractNode2(ans.get(2));
//            System.out.println(textInfo);
//            if (textInfoService.updateTextInfo(textInfo) == 1) {
//                return ResponseUtil.ok(textInfo);
//            } else {
//                return ResponseUtil.fail();
//            }
//        }
//    }
//
//    /**
//     * 审核节点，status为1审核不通过，2代表审核通过
//     * @param textInfo
//     * @return
//     */
//    @Transactional(rollbackFor = Exception.class)
//    @PostMapping("/updateGraph")
//    public Object updateGraph(@RequestBody TextInfo textInfo) {
//        if (textInfoService.updateTextStatus(textInfo) == 0) {
//            return ResponseUtil.fail();
//        }
//        // 为2表示审核通过
//        if (textInfo.getStatus() == 2) {
//            TextInfo t = textInfoService.getTextInfoById(textInfo.getId());
//            Integer code = textInfoService.updateGraph(t.getExtractNode1(), t.getExtractNode2());
//            if (code == 1) {
//                return ResponseUtil.ok();
//            } else {
//                return ResponseUtil.fail();
//            }
//        }
//        return ResponseUtil.ok();
//    }
//
//    /**
//     * 获取所有未审核的节点
//     * @return
//     */
//    @GetMapping("/allText")
//    public Object getAllText() {
//        return ResponseUtil.ok(textInfoService.getAllText());
//    }
//}
