package com.beeplay.core.db.ds;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 * @author chenlf
 */
public class MultipleDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        Object key = DataSourceContext.getCustomerType();
        if (key != null) {
            logger.info("当前线程使用的数据源标识为 [ " + key.toString() + " ].");
        }
        return key;
    }
}
