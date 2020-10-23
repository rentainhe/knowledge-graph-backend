package xmu.vis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import xmu.vis.domain.PersonnelInformation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface PersonnelInformationMapper {

    public List<PersonnelInformation> getAllPersonnelInformation();

    public PersonnelInformation getPersonnelInformationByUnitId(String unitId);

    public PersonnelInformation getPersonnelInformationByUnitName(String unitName);

    public PersonnelInformation getPersonnelInformationByPersonName(String personName);



}
