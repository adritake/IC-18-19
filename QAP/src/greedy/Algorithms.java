package greedy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import genetic.Chromosome;
import genetic.Population;

public class Algorithms {

	/**
	 * This greedy algorithm initialize the a chromosome using the nearest neighbour strategy
	 * @param flows
	 * @param distances
	 * @param fileSize
	 * @return a Chromosome initialize by this strategy
	 */
	public static Chromosome constructive( ArrayList<Integer> flows, ArrayList<Integer> distances, int fileSize ) {
		
		
		// Array of the permutations of factories
		int []positions = new int[fileSize];
		
		// Initialize an array of the factories that haven't been added to the positions
		ArrayList<Integer> remaining_factories = new ArrayList();
		for(int i = 0; i < fileSize; i++) {
			remaining_factories.add(i);
		}
		
		// Initialize an array of the positions that haven't been filled
		ArrayList<Integer> remaining_positions = new ArrayList();
		for(int i = 0; i < fileSize; i++) {
			remaining_positions.add(i);
		}
		
		// Calculate the first factory to add to the array
		Integer randomFac = ThreadLocalRandom.current().nextInt(0, fileSize);
		Integer randomPos = ThreadLocalRandom.current().nextInt(0, fileSize);
		System.out.println("Randomfac = " + randomFac + " Randompos = " + randomPos );
		positions[randomPos] = randomFac;
		Integer last_position = randomPos;
		Integer last_factory = randomFac;
		remaining_factories.remove((Integer)randomFac);
		remaining_positions.remove((Integer)randomPos);
		
		int filled_positions = 1;
		while(filled_positions < fileSize) {
			
			//Calcultate the closest position to the current one
			int closest_distance = Integer.MAX_VALUE;
			int closest_position = remaining_positions.get(0);
			
			for( int i = 0; i < remaining_positions.size(); i++) {
				Integer position = remaining_positions.get(i);
				int new_dist = distances.get(fileSize * last_position + position );
				if(  new_dist < closest_distance ) {
					closest_distance = new_dist;
					closest_position = position;
				}
					
			}
			// Now the closest position is set in the closest_position variable
			//System.out.println("La posición más cercana a " + last_position + " es " + closest_position + " con " + closest_distance);
		
			//Calculate the highest flow to the current factory
			int highest_flow = Integer.MIN_VALUE;
			int highest_factory = remaining_factories.get(0);
			
			for( int i = 0; i < remaining_factories.size(); i++) {
				Integer factory = remaining_factories.get(i);
				int new_flow = flows.get(last_factory*fileSize + factory);
				if( new_flow > highest_flow) {
					highest_flow = new_flow;
					highest_factory = factory;
				}
			}
			// Now the factory with the highest flow to the last factory is set in the highest_factory variable
			//System.out.println("La factoría con más flujo a " + last_factory + " es " + highest_factory + " con " + highest_flow);

			
			
			//Add the new factory to the new position
			positions[closest_position] = highest_factory;
			//Update variables
			remaining_positions.remove((Integer)closest_position);
			remaining_factories.remove((Integer)highest_factory);
			last_position = closest_position;
			last_factory = highest_factory;
			
			filled_positions++;
			/*
			for(int i = 0; i< positions.length; i++)
				System.out.print(positions[i] + " ");
			System.out.print("\n");
			System.out.println("filled positions----------" + filled_positions);
			*/
		}
		
		ArrayList<Integer> c = new ArrayList();
		while( c.size() < fileSize ) {
			
			Integer randomNum = ThreadLocalRandom.current().nextInt(0, fileSize);
			if(!c.contains(randomNum))
				c.add(randomNum);
			
		}
		return new Chromosome(positions);
	}
	
	
	/**
	 * This greedy algorithm finds a local optimum of a chromosome with a 2-opt transposition algorithm
	 * @param chro
	 * @param flows
	 * @param distances
	 * @param fileSize
	 * @return the improved chromosome
	 */
	public static Chromosome transposition( Chromosome chro, ArrayList<Integer> flows, ArrayList<Integer> distances, int fileSize ) {
		
		ArrayList<Integer>positions = new ArrayList(chro.getChromosome());
		ArrayList<Integer> mejor;
		
		do {
			mejor = new ArrayList(positions);

			for( int i = 0; i < positions.size(); i++) {
				for( int j = i+1; j < positions.size(); j++) {
					ArrayList<Integer> swaped = swap(positions, i, j);
					if( Population.calculateFitness(new Chromosome(swaped), flows, distances, fileSize) > 
						Population.calculateFitness(new Chromosome(positions), flows, distances, fileSize)	) {
						positions =  swaped;
					}
				}

			}

			
		}while(!mejor.equals(positions));
		
				
		return new Chromosome(positions);
	}
	
	/**
	 * Method to swap two elements of an ArrayList<Integer>
	 * @param arr ArrayList<Integer>
	 * @param i
	 * @param j
	 * @return the swaped array
	 */
	public static ArrayList<Integer> swap (ArrayList<Integer> arr,int i, int j){
		
		ArrayList<Integer> swaped = new ArrayList(arr);
		
		if(i > 0 && i < swaped.size() && j > 0 && j < swaped.size()) {
			Integer aux = swaped.get(i);
			swaped.set(i, swaped.get(j));
			swaped.set(j, aux);
		}
		return swaped;	
	}
	
	
}
