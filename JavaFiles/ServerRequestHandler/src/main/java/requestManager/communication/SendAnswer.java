package requestManager.communication;

public interface SendAnswer {

	/**
	 * @param msg dati sotto forma di stringa
	 */
	public void send(final String username, final String msg);
}
