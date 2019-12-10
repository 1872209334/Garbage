package com.qf.common.mybatis;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;


import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;
import com.qf.common.mybatis.DataBaseType.DatabaseType;

/**
 * mybatis配置类 author Vareck
 */
@Configuration
@MapperScan(basePackages = "com.qf.mapper.fire", sqlSessionFactoryRef = "fireSqlSessionTemplate")
public class FireMyBatisConfig {

	@Value("${spring.datasource.fire.url}")
	private String url;
	@Value("${spring.datasource.fire.username}")
	private String username;
	@Value("${spring.datasource.fire.password}")
	private String password;
	@Value("${mysql.datasource.fire.minPoolSize}")
	private int minPoolSize;
	@Value("${mysql.datasource.fire.maxPoolSize}")
	private int maxPoolSize;
	@Value("${mysql.datasource.fire.maxLifetime}")
	private int maxLifetime;
	@Value("${mysql.datasource.fire.borrowConnectionTimeout}")
	private int borrowConnectionTimeout;
	@Value("${mysql.datasource.fire.loginTimeout}")
	private int loginTimeout;
	@Value("${mysql.datasource.fire.maintenanceInterval}")
	private int maintenanceInterval;
	@Value("${mysql.datasource.fire.maxIdleTime}")
	private int maxIdleTime;
	@Value("${mysql.datasource.fire.tQuery}")
	private String tQuery;

	@Value("${spring.datasource.admin.url}")
	private String ad_url;
	@Value("${spring.datasource.admin.username}")
	private String ad_username;
	@Value("${spring.datasource.admin.password}")
	private String ad_password;
	@Value("${mysql.datasource.admin.minPoolSize}")
	private int ad_minPoolSize;
	@Value("${mysql.datasource.admin.maxPoolSize}")
	private int ad_maxPoolSize;
	@Value("${mysql.datasource.admin.maxLifetime}")
	private int ad_maxLifetime;
	@Value("${mysql.datasource.admin.borrowConnectionTimeout}")
	private int ad_borrowConnectionTimeout;
	@Value("${mysql.datasource.admin.loginTimeout}")
	private int ad_loginTimeout;
	@Value("${mysql.datasource.admin.maintenanceInterval}")
	private int ad_maintenanceInterval;
	@Value("${mysql.datasource.admin.maxIdleTime}")
	private int ad_maxIdleTime;
	@Value("${mysql.datasource.admin.tQuery}")
	private String ad_tQuery;

	@Autowired
	private Environment env;

	/**
	 * 创建数据源(数据源的名称：方法名可以取为XXXDataSource(),XXX为数据库名称,该名称也就是数据源的名称)
	 */

	@Bean(name = "adminDataSource")
	public DataSource adminDataSource() throws Exception {
		MysqlXADataSource mysqlXADataSource = new MysqlXADataSource();
		mysqlXADataSource.setUrl(this.ad_url);
		mysqlXADataSource.setPinGlobalTxToPhysicalConnection(true);
		mysqlXADataSource.setPassword(this.ad_password);
		mysqlXADataSource.setUser(this.ad_username);
		mysqlXADataSource.setPinGlobalTxToPhysicalConnection(true);
		AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
		atomikosDataSourceBean.setXaDataSource(mysqlXADataSource);
		atomikosDataSourceBean.setUniqueResourceName("adminDataSource");
		atomikosDataSourceBean.setMinPoolSize(this.ad_minPoolSize);
		atomikosDataSourceBean.setMaxPoolSize(this.ad_maxPoolSize);
		atomikosDataSourceBean.setMaxLifetime(this.ad_maxLifetime);
		atomikosDataSourceBean.setBorrowConnectionTimeout(this.ad_borrowConnectionTimeout);
		atomikosDataSourceBean.setLoginTimeout(this.ad_loginTimeout);
		atomikosDataSourceBean.setMaintenanceInterval(this.ad_maintenanceInterval);
		atomikosDataSourceBean.setMaxIdleTime(this.ad_maxIdleTime);
		atomikosDataSourceBean.setTestQuery(this.ad_tQuery);
		return atomikosDataSourceBean;
	}

	@Bean(name = "fireDataSource")
	public DataSource fireDataSource() throws Exception {
		MysqlXADataSource mysqlXADataSource = new MysqlXADataSource();
		mysqlXADataSource.setUrl(this.url);
		mysqlXADataSource.setPinGlobalTxToPhysicalConnection(true);
		mysqlXADataSource.setPassword(this.password);
		mysqlXADataSource.setUser(this.username);
		mysqlXADataSource.setPinGlobalTxToPhysicalConnection(true);
		AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
		atomikosDataSourceBean.setXaDataSource(mysqlXADataSource);
		atomikosDataSourceBean.setUniqueResourceName("fireDataSource");
		atomikosDataSourceBean.setMinPoolSize(this.minPoolSize);
		atomikosDataSourceBean.setMaxPoolSize(this.maxPoolSize);
		atomikosDataSourceBean.setMaxLifetime(this.maxLifetime);
		atomikosDataSourceBean.setBorrowConnectionTimeout(this.borrowConnectionTimeout);
		atomikosDataSourceBean.setLoginTimeout(this.loginTimeout);
		atomikosDataSourceBean.setMaintenanceInterval(this.maintenanceInterval);
		atomikosDataSourceBean.setMaxIdleTime(this.maxIdleTime);
		atomikosDataSourceBean.setTestQuery(this.tQuery);
		return atomikosDataSourceBean;

	}

	@Bean
	@Primary
	public DynamicDataSource dataSource(@Qualifier("fireDataSource") DataSource fireDataSource,
			@Qualifier("adminDataSource") DataSource adminDataSource) {
		Map<Object, Object> targetDataSources = new HashMap<>();

		targetDataSources.put(DatabaseType.hixent_user, adminDataSource);
		targetDataSources.put(DatabaseType.hixent_arc_system, fireDataSource);
		DynamicDataSource dataSource = new DynamicDataSource();
		dataSource.setTargetDataSources(targetDataSources);// 该方法是AbstractRoutingDataSource的方法
		dataSource.setDefaultTargetDataSource(adminDataSource);// 默认的datasource设置为myTestDbDataSource

		return dataSource;
	}

	@Bean(name = "fireSqlSessionTemplate")
	public SqlSessionFactory fireSqlSessionFactory(DynamicDataSource ds) {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(ds);
		bean.setTypeAliasesPackage("com.qf.model.fire");
		// 添加XML目录
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

		try {
			bean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
			bean.setMapperLocations(resolver.getResources("classpath:mapper/*/*.xml"));
			return bean.getObject();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	

	@Bean(name = "fireSqlSessionTemplate")
	public SqlSessionTemplate fireSqlSessionTemplate(
			@Qualifier("fireSqlSessionTemplate") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	/**
	 * 配置事务管理器
	 */
	@Bean
	public DataSourceTransactionManager transactionManager(DynamicDataSource dataSource) throws Exception {
		return new DataSourceTransactionManager(dataSource);
	}
}