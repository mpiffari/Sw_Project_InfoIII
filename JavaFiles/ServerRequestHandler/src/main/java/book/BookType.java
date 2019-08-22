package book;

public enum BookType {
	ACTION("ACTION"),
	ADVENTURE("ADVENTURE"),
	THRILLER("THRILLER"),
	FANTASY("FANTASY"),
	HORROR("HORROR"),
	FAIRYSTORY("FAIRYSTORY"),
	OTHER("OTHER");
	
    private String description;

    private BookType(String desc) {
        this.description = desc;
    }

    public String getSigla() {
        return description;
    }
}
