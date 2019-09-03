package user;

public class UserLocalizationInfo {
	public double radius;
	public double lat;
	public double longit;
	
	public UserLocalizationInfo() {}
	
	public UserLocalizationInfo(double lat, double longit) {
		this.lat = lat;
		this.longit = longit;
	}
	
	public String toString() {
		return "Lat: " + this.lat + " Longit: " + this.longit + " Raggio d'azione: " + this.radius;
	}
}
