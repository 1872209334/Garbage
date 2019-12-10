package com.qf.service.fire.impl;

import com.qf.mapper.fire.HixentArcSiteMapper;
import com.qf.model.fire.HixentArcSite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HixentArcSiteServiceImpl implements HixentArcSiteService {

    @Autowired
    private HixentArcSiteMapper hixentArcSiteMapper;

    @Override
    public Integer insertNewProject(HixentArcSite params) {
        return hixentArcSiteMapper.insertNewProject(params);
    }

    @Override
    public Integer insertNewDevice(String siteCode, String deviceType,String equipCode) {
        String id = siteCode+equipCode;
        return hixentArcSiteMapper.insertNewDevice(id,siteCode,deviceType);
    }
}
