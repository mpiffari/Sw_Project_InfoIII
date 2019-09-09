package requestManager;

public interface ProcessRequest {

	/**
	 * Interfaccia implementata dalla classe che si occupa di andare a processare
	 * la richiesta ricevuta dal client.
	 * 
	 * @param msg dati sotto forma di stringa
	 */
	public void process(String msg, String username);
}
