package xmu.vis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import xmu.vis.domain.RelationTuple;
import xmu.vis.controller.VO.*;

import javax.management.relation.Relation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
@Component
public interface RelationTupleMapper {
    public List<RelationTuple> getAllRelation();//获取所有关系
    public Set<String> getAllRelationTypeSet();//获取关系类型集合
    public List<RelationTuple> getRelationByChildId(String child_Id);//根据 子节点ID 获得关系
    public List<RelationTuple> getRelationByChildName(String child_Name);//根据 子节点名称 获得关系

    public List<RelationTuple> getRelationByFatherId(String father_Id);//根据 父节点ID 获得关系
    public List<RelationTuple> getRelationByFatherName(String father_Name);//根据 父节点名称 获得关系

    public List<RelationTuple> getRelationByFatherIdandChildId(String father_Id, String child_Id);//根据 父子Id 获得关系
    public List<RelationTuple> getRelationByFatherNameandChildName(String father_Name, String child_Name);//根据 父子Name 获得关系

    public RelationTuple checkRelationTupleexist(RelationTuple relationTuple);//检查<关系三元组>表中是否存在该关系(默认存在只有一个)

    public Integer addNewRelation(RelationTuple newRelation);//增加没有的关系
    public Integer deleteExistRelation(RelationTuple targetRelation);//删除已有的关系
    public Integer updateRelation(@Param("oldRelationTuple") RelationTuple oldRelationTuple, String newRelationName);//更改已有的关系 map中key为原三元组 value为更改后的关系

    public List<String> showAllColumn();//返回表结构
}
