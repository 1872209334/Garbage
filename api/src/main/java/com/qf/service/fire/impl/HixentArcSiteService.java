package com.qf.service.fire.impl;

import com.qf.model.fire.HixentArcSite;

public interface HixentArcSiteService {

    Integer insertNewProject(HixentArcSite params);

    Integer insertNewDevice(String siteCode,String deviceType, String equipCode);

}
