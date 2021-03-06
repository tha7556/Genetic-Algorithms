package TSP;


public class Route {
	private City[] cities;
	private int distance = 0;
	
	public Route(City[] cities) {
		this.cities = cities;
		getDistance();
	}
	public City[] getCities() {
		return cities;
	}
	public int getDistance() {
		if(distance == 0.0) {
			for(int i = 0; i < cities.length; i++) {
				if(i+1 < cities.length) {
					distance += cities[i].distanceTo(cities[i+1]);
				}
				else {
					distance += cities[i].distanceTo(cities[0]);
				}
			}
		}
		return distance;
	}
	public double getFitness() {
		return 1/(double)distance;
	}
	@Override
	public String toString() {
		String result = "";
		for(City city : cities) {
			result += city.getName() + "-";
		}
		result += cities[0].getName();
		return result.trim();
	}
}
