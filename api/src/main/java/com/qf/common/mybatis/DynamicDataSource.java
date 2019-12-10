package com.qf.common.mybatis;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource{
	/**
	 * 动态数据源（需要继承AbstractRoutingDataSource）
	 */

	  protected Object determineCurrentLookupKey() {
	    return DatabaseContextHolder.getDatabaseType();
	  }
}
