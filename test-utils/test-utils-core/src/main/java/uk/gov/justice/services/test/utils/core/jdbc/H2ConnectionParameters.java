package uk.gov.justice.services.test.utils.core.jdbc;

import java.sql.Connection;

/**
 * Default connection parameters required to create a JDBC {@link Connection}
 * to an in memory H2 database
 */
public interface H2ConnectionParameters {

    String URL = "jdbc:h2:mem:test;MV_STORE=FALSE;MVCC=FALSE";
    String USERNAME = "sa";
    String PASSWORD = "sa";
    String DRIVER_CLASS = "org.h2.Driver";
}
