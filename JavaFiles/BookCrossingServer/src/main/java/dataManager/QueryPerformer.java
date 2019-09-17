package dataManager;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 
 * Interfaccia implementata dall'oggetto incaricato di andare ad eseguire le queries verso il db.
 * 
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */
public interface QueryPerformer {
	public abstract ResultSet executeStatement(Statement stmt);
}
