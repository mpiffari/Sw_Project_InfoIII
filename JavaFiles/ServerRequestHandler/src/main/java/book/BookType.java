package book;

/**
 * 
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */
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
