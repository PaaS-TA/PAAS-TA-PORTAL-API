package org.openpaas.paasta.portal.api.config.service;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Data Configuration
 * @author nawkm
 * @version 1.0
 * @since 2016.4.4 최초작성
 * @midify 2017.08.4 by ijlee
 */
public abstract class DataConfig {

    @Bean
    public abstract DataSource dataSource();

}

@Configuration
@EnableTransactionManagement
class CcDataConfig extends DataConfig {

    @Autowired
    private Environment env;
    @Value("${spring.jdbc}") String jdbcName;
    @Value("${spring.datasource.cc.jdbc}") String ccJdbcName;
        /**
     * ccdb Data source data source.
     *
     * @return the data source
     */
    @Bean(name = "ccDataSource", destroyMethod = "close")
    public DataSource dataSource() {
        DataSource ccDataSource = new DataSource();

        jdbcName = ccJdbcName.isEmpty()?jdbcName:ccJdbcName;

        ccDataSource.setDriverClassName(env.getRequiredProperty("spring.datasource."+jdbcName+".driverClassName"));
        ccDataSource.setUrl(env.getRequiredProperty("spring.datasource.cc.url"));
        ccDataSource.setUsername(env.getRequiredProperty("spring.datasource.cc.username"));
        ccDataSource.setPassword(env.getRequiredProperty("spring.datasource.cc.password"));

        return ccDataSource;
    }

    @Bean(name = "ccTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("ccDataSource") DataSource ccDataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(ccDataSource);
        transactionManager.setGlobalRollbackOnParticipationFailure(false);
        return transactionManager;
    }
}

@Configuration
@EnableTransactionManagement
class PortalDataConfig extends DataConfig {

    @Autowired
    private Environment env;
    @Value("${spring.jdbc}") String jdbcName;
    @Value("${spring.datasource.portal.jdbc}") String portalJdbcName;

    /**
     * portaldb Data source data source.
     *
     * @return the data source
     */
    @Bean(name = "portalDataSource", destroyMethod = "close")
    public DataSource dataSource() {
        DataSource portalDataSource = new DataSource();
        jdbcName = portalJdbcName.isEmpty()?jdbcName:portalJdbcName;

        portalDataSource.setDriverClassName(env.getRequiredProperty("spring.datasource."+jdbcName+".driverClassName"));
        portalDataSource.setUrl(env.getRequiredProperty("spring.datasource.portal.url"));
        portalDataSource.setUsername(env.getRequiredProperty("spring.datasource.portal.username"));
        portalDataSource.setPassword(env.getRequiredProperty("spring.datasource.portal.password"));

        return portalDataSource;
    }

    @Bean(name = "portalTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("portalDataSource") DataSource portalDataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(portalDataSource);
        transactionManager.setGlobalRollbackOnParticipationFailure(false);
        return transactionManager;
    }
}

@Configuration
@EnableTransactionManagement
class UaaDataConfig extends DataConfig {

    @Autowired
    private Environment env;
    @Value("${spring.jdbc}") String jdbcName;
    @Value("${spring.datasource.uaa.jdbc}") String uaaJdbcName;
    /**
     * uaadb Data source data source.
     *
     * @return the data source
     */
    @Primary
    @Bean(name = "uaaDataSource", destroyMethod = "close")
    public DataSource dataSource() {

        DataSource uaaDataSource = new DataSource();
        jdbcName = uaaJdbcName.isEmpty()?jdbcName:uaaJdbcName;
        uaaDataSource.setDriverClassName(env.getRequiredProperty("spring.datasource."+jdbcName+".driverClassName"));
        uaaDataSource.setUrl(env.getRequiredProperty("spring.datasource.uaa.url"));
        uaaDataSource.setUsername(env.getRequiredProperty("spring.datasource.uaa.username"));
        uaaDataSource.setPassword(env.getRequiredProperty("spring.datasource.uaa.password"));

        return uaaDataSource;
    }

    @Primary
    @Bean(name = "uaaTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("uaaDataSource") DataSource uaaDataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(uaaDataSource);
        transactionManager.setGlobalRollbackOnParticipationFailure(false);
        return transactionManager;
    }
}

@Configuration
@EnableTransactionManagement
class AutoScailingDataConfig extends DataConfig {

    @Autowired
    private Environment env;
    // 임시이므로
    String jdbcName = "mysql";
    @Value("${spring.datasource.autoScailing.jdbc}") String autoScailingJdbcName;

    @Bean(name = "autoScailingDataSource", destroyMethod = "close")
    public DataSource dataSource() {

        DataSource autoScailingDataSource = new DataSource();
        jdbcName = autoScailingJdbcName.isEmpty()?jdbcName:autoScailingJdbcName;
        autoScailingDataSource.setDriverClassName(env.getRequiredProperty("spring.datasource."+jdbcName+".driverClassName"));
        autoScailingDataSource.setUrl(env.getRequiredProperty("spring.datasource.autoScailing.url"));
        autoScailingDataSource.setUsername(env.getRequiredProperty("spring.datasource.autoScailing.username"));
        autoScailingDataSource.setPassword(env.getRequiredProperty("spring.datasource.autoScailing.password"));

        return autoScailingDataSource;
    }

    @Bean(name = "autoScailingTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("autoScailingDataSource") DataSource autoScailingDataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(autoScailingDataSource);
        transactionManager.setGlobalRollbackOnParticipationFailure(false);
        return transactionManager;
    }
}