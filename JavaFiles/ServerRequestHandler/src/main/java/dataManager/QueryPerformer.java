package dataManager;

import java.sql.ResultSet;
import java.sql.Statement;

public interface QueryPerformer {
	
	public abstract ResultSet executeStatement(Statement stmt);
}
