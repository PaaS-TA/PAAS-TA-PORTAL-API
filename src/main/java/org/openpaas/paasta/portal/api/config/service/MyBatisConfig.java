package org.openpaas.paasta.portal.api.config.service;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.openpaas.paasta.portal.api.config.service.surport.AutoScailing;
import org.openpaas.paasta.portal.api.config.service.surport.Cc;
import org.openpaas.paasta.portal.api.config.service.surport.Portal;
import org.openpaas.paasta.portal.api.config.service.surport.Uaa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * Created by mg on 2016-05-20.
 */
public class MyBatisConfig {

    @Autowired
    private Environment env;

    @Value("${spring.jdbc}") String jdbcName;
    @Value("${spring.datasource.portal.jdbc}") String portalJdbcName;



    public static final String BASE_PACKAGE = "org.openpaas.paasta.portal.api";
    public static final String MYSQL_MAPPER_LOCATIONS_PATH = "classpath:mapper/mysql/**/*.xml";
    public static final String POSTGRES_MAPPER_LOCATIONS_PATH = "classpath:mapper/postgresql/**/*.xml";
    public static final String AUTOSCAIL_MAPPER_LOCATIONS_PATH = "classpath:mapper/autoscail/**/*.xml";

    protected void configureSqlSessionFactory(SqlSessionFactoryBean sessionFactoryBean, DataSource dataSource) throws IOException {
        PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
        sessionFactoryBean.setDataSource(dataSource);
        jdbcName = portalJdbcName.isEmpty()?jdbcName:portalJdbcName;

        if("mysql".equals(jdbcName)) {
            sessionFactoryBean.setMapperLocations(pathResolver.getResources(MYSQL_MAPPER_LOCATIONS_PATH));
        }
        if("postgresql".equals(jdbcName)) {
            sessionFactoryBean.setMapperLocations(pathResolver.getResources(POSTGRES_MAPPER_LOCATIONS_PATH));
        }
    }
    //TODO
    /**임시 삭제예정*/

    protected void autoscailConfigureSqlSessionFactory(SqlSessionFactoryBean sessionFactoryBean, DataSource dataSource) throws IOException {
        PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(pathResolver.getResources(AUTOSCAIL_MAPPER_LOCATIONS_PATH));
    }
}

@Configuration
@MapperScan(basePackages = MyBatisConfig.BASE_PACKAGE, annotationClass = Cc.class, sqlSessionFactoryRef = "ccSqlSessionFactory")
class CcMyBatisConfig extends MyBatisConfig {

    @Bean
    public SqlSessionFactory ccSqlSessionFactory(@Qualifier("ccDataSource") DataSource ccDataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        configureSqlSessionFactory(sessionFactoryBean, ccDataSource);
        return sessionFactoryBean.getObject();
    }
}

@Configuration
@MapperScan(basePackages = MyBatisConfig.BASE_PACKAGE, annotationClass = Portal.class, sqlSessionFactoryRef = "portalSqlSessionFactory")
class PortalMyBatisConfig extends MyBatisConfig {

    @Bean
    public SqlSessionFactory portalSqlSessionFactory(@Qualifier("portalDataSource") DataSource portalDataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        configureSqlSessionFactory(sessionFactoryBean, portalDataSource);
        return sessionFactoryBean.getObject();
    }
}

@Configuration
@MapperScan(basePackages = MyBatisConfig.BASE_PACKAGE, annotationClass = Uaa.class, sqlSessionFactoryRef = "uaaSqlSessionFactory")
class UaaMyBatisConfig extends MyBatisConfig {

    @Bean
    public SqlSessionFactory uaaSqlSessionFactory(@Qualifier("uaaDataSource") DataSource uaaDataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        configureSqlSessionFactory(sessionFactoryBean, uaaDataSource);
        return sessionFactoryBean.getObject();
    }
}

@Configuration
@MapperScan(basePackages = MyBatisConfig.BASE_PACKAGE, annotationClass = AutoScailing.class, sqlSessionFactoryRef = "autoScailingSqlSessionFactory")
class AutoScailingMyBatisConfig extends MyBatisConfig {

    @Bean
    public SqlSessionFactory autoScailingSqlSessionFactory(@Qualifier("autoScailingDataSource") DataSource autoScailingDataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        autoscailConfigureSqlSessionFactory(sessionFactoryBean, autoScailingDataSource);
        return sessionFactoryBean.getObject();
    }
}