package com.qf.common.mybatis;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class Datasource extends AbstractRoutingDataSource  {
  
	public Datasource() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected Object determineCurrentLookupKey() {
		
		return DatabaseContextHolder.getDatabaseType();
	}

}
