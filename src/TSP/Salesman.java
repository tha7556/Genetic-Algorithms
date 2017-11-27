package TSP;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
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
		double total = 0.0;
		for(Route r : routes)
			total += r.getFitness();
		Random rand = new Random();
		//Parent 1
		double target = rand.nextDouble()*total; 
		double current = 0.0;
		int i = 0;
		while(current < target) {
			if(i == routes.length)
				i = 0;
			current += routes[i].getFitness();
			i++;
		}
		i--;
		if(i < 0)
			i = routes.length-1;
		parent1 = routes[i];
		//Parent 2
		target = rand.nextDouble()*total; 
		current = 0.0;
		i = 0;
		while(current < target) {
			if(i == routes.length)
				i = 0;
			current += routes[i].getFitness();
			i++;
		}
		i--;
		if(i < 0)
			i = routes.length-1;
		parent2 = routes[i];
		
		return new Route[] {parent1, parent2};
	}
	public Route createChild(Route parent1, Route parent2) {
		City[] arr = new City[cities.length];
		Random rand = new Random();
		int start = (int) (rand.nextDouble() * cities.length);
		int end = (int) (rand.nextDouble() * cities.length);
		while(end <= start) {
			end = (int) (rand.nextDouble() * cities.length);
			start = (int) (rand.nextDouble() * cities.length);
		}
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
		newRoutes[0] = (getBestRoute());
		for(int i = 1; i < routes.length; i++) {
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
	public double getAverageDistance() {
		double avg = 0.0;
		for(Route r : routes) {
			avg += r.getDistance();
		}
		return avg / routes.length;
	}
	public Route getBestRoute() {
		Route result = routes[0];
		for(Route r : routes) {
			if(r.getDistance() < result.getDistance())
				result = r;
		}
		return result;
	}
	public Route getWorstRoute() {
		Route result = routes[0];
		for(Route r : routes) {
			if(r.getFitness() < result.getFitness())
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
		Salesman s = null;
		String[] letters = new String[] {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T"};
		int[] xVals = new int[]{60,20,40,60,20,20,60,100,160,200,180,120,140,180,200,180,140,100,100,80};
		int[] yVals = new int[]{200,160,120,80,40,20,20,40,20,40,60,80,140,100,160,200,180,120,160,180};
		City[] cities = new City[letters.length];
		for(int i = 0; i < xVals.length; i++)
			//cities[i] = new City(letters[i],rand.nextInt(200),rand.nextInt(200));
			cities[i] = new City(letters[i],xVals[i],yVals[i]);
		try {
			File outFile = new File ("Cities.csv"); 
			FileWriter fWriter = new FileWriter (outFile); 
			PrintWriter pWriter = new PrintWriter (fWriter);
			
			pWriter.println("Name,X,Y");
			for(City c : cities) {
				pWriter.println(c.getName()+","+c.getX()+","+c.getY());
			}
			
			fWriter.close();
			pWriter.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		try {
			File outFile = new File ("Results.csv"); 
			FileWriter fWriter = new FileWriter (outFile); 
			PrintWriter pWriter = new PrintWriter (fWriter);
			
			pWriter.println("Trial,Average Fitness,Average Distance,Best Fitness,Best Distance,Best Route,Worst Fitness,Worst Distance,Worst Route");
			s = new Salesman(cities);
			
			File outFile2 = new File ("InitialRoute.csv"); 
			FileWriter fWriter2 = new FileWriter (outFile2); 
			PrintWriter pWriter2 = new PrintWriter (fWriter2);
			
			for(City c : s.getBestRoute().getCities()) {
				pWriter2.println(c.getName()+","+c.getX()+","+c.getY());
			}
			pWriter2.println(s.getBestRoute().getCities()[0].getName()+","+s.getBestRoute().getCities()[0].getX()+","+s.getBestRoute().getCities()[0].getY());
			fWriter2.close();
			pWriter2.close();
			
			System.out.println(s.getAverageFitness());
			pWriter.println(1+","+s.getAverageFitness()+","+s.getAverageDistance()+","+s.getBestRoute().getFitness()+","+s.getBestRoute().getDistance()+","+s.getBestRoute()+","+s.getWorstRoute().getFitness()+","+s.getWorstRoute().getDistance()+","+s.getWorstRoute());
			int i = 0;
			double target = .00116;
			while(s.getBestRoute().getFitness() < target) {
				s.evolve();
				if(i < 10 || i % 5 == 0 || s.getBestRoute().getFitness() >= target) {
					System.out.println(s.getBestRoute().getFitness()+"     "+s.getBestRoute()+"  "+i);
					pWriter.println((i+1)+","+s.getAverageFitness()+","+s.getAverageDistance()+","+s.getBestRoute().getFitness()+","+s.getBestRoute().getDistance()+","+s.getBestRoute()+","+s.getWorstRoute().getFitness()+","+s.getWorstRoute().getDistance()+","+s.getWorstRoute());
				}
				i++;
			}
			
			fWriter.close();
			pWriter.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			File outFile = new File ("BestRoute.csv"); 
			FileWriter fWriter = new FileWriter (outFile); 
			PrintWriter pWriter = new PrintWriter (fWriter);
			
			for(City c : s.getBestRoute().getCities()) {
				pWriter.println(c.getName()+","+c.getX()+","+c.getY());
			}
			pWriter.println(s.getBestRoute().getCities()[0].getName()+","+s.getBestRoute().getCities()[0].getX()+","+s.getBestRoute().getCities()[0].getY());
			fWriter.close();
			pWriter.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
