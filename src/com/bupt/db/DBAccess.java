package com.bupt.db;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * 访问数据库类
 */
public class DBAccess {
	private volatile static SqlSessionFactory sqlSessionFactory;

	public static SqlSessionFactory getSqlSessionFactory() throws IOException {
		if (sqlSessionFactory == null) {
			synchronized (SqlSessionFactory.class) {
				if (sqlSessionFactory == null) {
					// 通过配置文件获取数据库连接信息
					Reader reader = Resources
							.getResourceAsReader("Configuration.xml");
					// 通过配置信息构建一个SqlSessionFactory
					sqlSessionFactory = new SqlSessionFactoryBuilder()
							.build(reader);
				}
			}
		}
		return sqlSessionFactory;
	}

}
