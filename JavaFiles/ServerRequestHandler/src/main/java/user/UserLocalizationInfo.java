package user;

public class UserLocalizationInfo {
	public double radius;
	public double lat;
	public double longit;
	
	public UserLocalizationInfo() {}
	
	public String toString() {
		return "Lat: " + this.lat + " Longit: " + this.longit + " Raggio d'azione: " + this.radius;
	}
}
