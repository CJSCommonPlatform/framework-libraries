package uk.gov.moj.cpp.jobmanager.it.util;



import java.util.Properties;


public class OpenEjbConfigurationBuilder {

    private final Properties configuration = new Properties();

    private OpenEjbConfigurationBuilder() {
    }

    public static OpenEjbConfigurationBuilder createOpenEjbConfigurationBuilder() {
        return new OpenEjbConfigurationBuilder();
    }

    public OpenEjbConfigurationBuilder addInitialContext() {
        this.configuration.put("java.naming.factory.initial", "org.apache.openejb.client.LocalInitialContextFactory");
        return this;
    }

    public OpenEjbConfigurationBuilder addPostgresViewStore() {
        return this.getPostgresqlConfig("viewStore");
    }

    public OpenEjbConfigurationBuilder addPostgresqlEventStore() {
        return this.getPostgresqlConfig("eventStore");
    }

    public OpenEjbConfigurationBuilder addPostgresqlJobStore() {
        return this.getPostgresqlConfig("jobStore");
    }

    public OpenEjbConfigurationBuilder addHttpEjbPort(int port) {
        this.configuration.put("httpejbd.port", Integer.toString(port));
        return this;
    }

    public Properties build() {
        return this.configuration;
    }

    private OpenEjbConfigurationBuilder getPostgresqlConfig(String dbName) {
        this.configuration.put(dbName, "new://Resource?type=DataSource");
        this.configuration.put(String.format("%s.JdbcDriver", dbName), "org.postgresql.Driver");
        this.configuration.put(String.format("%s.JdbcUrl", dbName), "jdbc:postgresql://localhost:5432/frameworkjobstore");
        this.configuration.put(String.format("%s.JtaManaged", dbName), true);
        this.configuration.put(String.format("%s.UserName", dbName), "framework");
        this.configuration.put(String.format("%s.Password", dbName), "framework");
        this.configuration.put(String.format("%s.defaultAutoCommit", dbName), false);
        this.configuration.put(String.format("%s.InitialSize", dbName), 10);
        this.configuration.put(String.format("%s.MaxTotal", dbName), -1);

        this.configuration.put(String.format("%s.LogSql", dbName), false);
        this.configuration.put(String.format("%s.TimeBetweenEvictionRuns", dbName), 1000);
        this.configuration.put(String.format("%s.MinEvictableIdleTime", dbName), 1000);
        this.configuration.put(String.format("%s.MaxWaitTime", dbName), 1000);
        this.configuration.put(String.format("%s.poolPreparedStatements", dbName), true);

        return this;
    }
}
