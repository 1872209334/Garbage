package com.qf.common.mybatis;

import com.qf.common.mybatis.DataBaseType.DatabaseType;

public class DatabaseContextHolder {
	/**
	 * 作用：
	 * 1、保存一个线程安全的DatabaseType容器
	 */
	
	  private static final ThreadLocal<DatabaseType> contextHolder = new ThreadLocal<>();
	   
	  public static void setDatabaseType(DatabaseType type){
	    contextHolder.set(type);
	  }
	   
	  public static DatabaseType getDatabaseType(){
	    return contextHolder.get();
	  }
	
}
