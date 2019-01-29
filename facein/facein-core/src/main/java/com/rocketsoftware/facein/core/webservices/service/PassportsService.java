package com.rocketsoftware.facein.core.webservices.service;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rocketsoftware.facein.core.webservices.entity.PassportsBean;
import com.rocketsoftware.facein.core.webservices.mapper.PassportsMapper;
 
/**
 * Created by sxc on 2019/1/18.
 */
@Service
public class PassportsService
{
    /**
     * 注入服务
     */
    @Autowired
    private PassportsMapper passportsMapper;
 
    public PassportsBean getPassport(String passportID)
    {
        return passportsMapper.getPassport(passportID);
    }
}
