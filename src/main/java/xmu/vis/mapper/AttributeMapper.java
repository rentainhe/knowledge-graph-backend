package xmu.vis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface AttributeMapper {

    public Map<String,Object> getAttributeById(String tableName,String id);

    public void addAttribute(String tableName, String attributeName);

    public void deleteAttribute(String tableName, String attributeName);

    public Integer updateAttribute(String tableName, Integer id, Map<String,Object> map);

    public void createTable(String tableName);

    public Integer existTable(String tableName);

    public List<String> getAllAttributeByName(String tableName);

    public void updateColumn(String tableName,String oldName, String newName);

    public void deleteTable(String tableName);
}
