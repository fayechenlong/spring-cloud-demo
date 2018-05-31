package com.beeplay.core.db.ds;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("mysqlDataSourceProperties")
@ConfigurationProperties(prefix = "mysql.datasource")
public class MysqlDataSourceProperties {

    private String driverClassName;
    private String url;
    private String username;
    private String password;

    private String driverClassNameRead;
    private String urlRead;
    private String usernameRead;
    private String passwordRead;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassNameRead() {
        return driverClassNameRead;
    }

    public void setDriverClassNameRead(String driverClassNameRead) {
        this.driverClassNameRead = driverClassNameRead;
    }

    public String getUrlRead() {
        return urlRead;
    }

    public void setUrlRead(String urlRead) {
        this.urlRead = urlRead;
    }

    public String getUsernameRead() {
        return usernameRead;
    }

    public void setUsernameRead(String usernameRead) {
        this.usernameRead = usernameRead;
    }

    public String getPasswordRead() {
        return passwordRead;
    }

    public void setPasswordRead(String passwordRead) {
        this.passwordRead = passwordRead;
    }
}
