package com.ncsu.wolfpub.db.connection;

import java.sql.Connection;

/**
 * 
 * Interface for DBCOnection. If application needs to support a new kind of DB,
 * Write a new class extends this interface.
 * 
 * @author vamsi
 *
 */
public interface IDBConnection {

	void init();

	Connection getConnection();

	void close();
}
