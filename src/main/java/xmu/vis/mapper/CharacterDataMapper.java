package xmu.vis.mapper;

import com.alibaba.druid.sql.visitor.functions.Char;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import xmu.vis.domain.CharacterData;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface CharacterDataMapper{

    public CharacterData getCharacterDataById(String personId);

    public List<CharacterData> getCharacterDataByName(String personName);

    public Integer insertCharacterData(CharacterData newCharacterData);

    public Integer deleteCharacterData(CharacterData characterData); //通过人员ID与人员姓名 定位

    public Integer modifyAttributeValue(String personId, Map<String, String> map);
}
