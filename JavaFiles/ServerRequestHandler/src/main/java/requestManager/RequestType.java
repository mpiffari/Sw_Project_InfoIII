package requestManager;

public enum RequestType {
	BOOK_REGISTRATION(0),
	BOOK_RESERVATION(1);
	
    private int description;

    private RequestType(int desc) {
        this.description = desc;
    }

    public int getType() {
        return description;
    }

	public static RequestType valueOf(int parseInt) {
		switch (parseInt) {
		case 0:
			return BOOK_REGISTRATION;
		case 1:
			return BOOK_RESERVATION;
		default:
			return null;
		}
	}
}
