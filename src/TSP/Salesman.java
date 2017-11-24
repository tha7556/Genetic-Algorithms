package TSP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import java.util.Random;

public class Salesman {
	private City[] cities;
	private final int POPULATION_SIZE = 200;
	private final double MUTATION_RATE = 0.01;
	private Route[] routes;
	
	public Salesman(City[] cities) {
		this.cities = cities;
		routes = generateInitialRoutes();
	}
	public Route[] generateInitialRoutes() {
		routes = new Route[POPULATION_SIZE];
		for(int i = 0; i < POPULATION_SIZE; i++) {
			Route r = new Route(shuffleArray(cities));
			routes[i] = r;
		}
		return routes;
	}
	public Route[] selectParents() {
		Route parent1 = routes[0], parent2 = routes[1];
		for(int i = 0; i < routes.length; i++) {
			if(routes[i].getFitness() > parent1.getFitness()) {
				parent2 = parent1;
				parent1 = routes[i];
			}
		}
		return new Route[] {parent1, parent2};
	}
	public Route createChild(Route parent1, Route parent2) {
		City[] arr = new City[cities.length];
		Random rand = new Random();
		int start = (int) (rand.nextDouble() * cities.length);
		int end = (int) (rand.nextDouble() * cities.length);
		while(end < start)
			end = (int) (rand.nextDouble() * cities.length);
		
		for(int i = 0; i < cities.length; i++) {
			if(i > start && i < end) {
				arr[i] = parent1.getCities()[i];
			}
		}
		for(City c : parent2.getCities()) {
			if(!Arrays.asList(arr).contains(c)) {
				for(int i = 0; i < arr.length; i++) {
					if(arr[i] == null) {
						arr[i] = c;
						break;
					}
				}
			}
		}
		return new Route(arr);
	}
	public Route mutate(Route route) {
		Random rand = new Random();
		for(int i = 0; i < route.getCities().length; i++) {
			if(rand.nextDouble() <= MUTATION_RATE) {
				int y = (int)(rand.nextDouble() * route.getCities().length);
				
				City temp = route.getCities()[i];
				route.getCities()[i] = route.getCities()[y];
				route.getCities()[y] = temp;
				
			}
		}
		return route;
	}
	public Route[] evolve() {
		Route[] newRoutes = new Route[routes.length];
		
		for(int i = 0; i < routes.length; i++) {
			Route[] parents = selectParents();
			Route child = createChild(parents[0],parents[1]);
			newRoutes[i] = mutate(child);
		}
		routes = newRoutes;
		return routes;
	}
	public City[] getCities() {
		return cities;
	}
	public double getAverageFitness() {
		double avg = 0.0;
		for(Route r : routes) {
			avg += r.getFitness();
		}
		return avg / routes.length;
	}
	public Route getBestRoute() {
		Route result = routes[0];
		for(Route r : routes) {
			if(r.getFitness() > result.getFitness())
				result = r;
		}
		return result;
	}
	public static City[] shuffleArray(City[] array)
	{
		City[] result = new City[array.length];
		ArrayList<City> list = new ArrayList<City>(array.length);
		for(City c : array)
			list.add(c);
		Collections.shuffle(list);
		for(int i = 0; i < list.size(); i++)
			result[i] = list.get(i);
		return result;
	}	
	public static void main(String[] args) {
		Random rand = new Random();
		String[] letters = new String[] {"A","B","C","D","E","F","G","H","I","J","K","L","M"};
		City[] cities = new City[13];
		for(int i = 0; i < letters.length; i++)
			cities[i] = new City(letters[i],rand.nextInt(100),rand.nextInt(100));
		
		Salesman s = new Salesman(cities);
		System.out.println(s.getAverageFitness());
		for(int i = 0; i < 100; i++) {
			s.evolve();
			System.out.println(s.getAverageFitness());
		}
	}
}
