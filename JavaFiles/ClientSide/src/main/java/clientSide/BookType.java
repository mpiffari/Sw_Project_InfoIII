package clientSide;

public enum BookType {
	ACTION("ACTION"),
	ADVENTURE("ADVENTURE"),
	THRILLER("THRILLER"),
	HORROR("HORROR");
	
    private String description;

    private BookType(String desc) {
        this.description = desc;
    }

    public String getSigla() {
        return description;
    }
}
