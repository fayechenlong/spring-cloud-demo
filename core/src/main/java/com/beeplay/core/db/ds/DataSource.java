package com.beeplay.core.db.ds;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    String name() default DataSource.DATA_SOURCE_WRITE;
    String DATA_SOURCE_READ = "dataSourceRead";
	String DATA_SOURCE_WRITE = "dataSourceWrite";
}
