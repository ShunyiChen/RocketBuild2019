package com.rocketsoftware.facein.core.webservices.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.rocketsoftware.facein.core.webservices.entity.PassportsBean;

/**
 * Created by sxc on 2019/1/18.
 */
@Mapper
public interface PassportsMapper
{
	//void addPassport(@Param("passportID") String passportID,@Param("photoPath") String photoPath);
	
	@Select("select * from passports where passportid=#{passportID}")
    public PassportsBean getPassport(String passportID);
}
