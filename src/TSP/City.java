package TSP;

public class City {
	public int x,y;
	String name;
	
	public City(String name, int x, int y) {
		this.name = name;
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public double distanceTo(City city) {
		return Math.sqrt((double)Math.pow(x-city.getX(), 2.0) + (double)Math.pow(y-city.getY(), 2.0));
	}
}
