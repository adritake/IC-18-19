package genetic;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import utils.MyMath;
import utils.QAPReader;

/**
 * Class that represents the population of the genetic algorithm
 * @author Adrián
 *
 */
public class Population {

	//All the chromosomes of the population
	private ArrayList<Chromosome> popu;
	
	private int fileSize;
	private ArrayList<Integer> flows;
	private ArrayList<Integer> distances;
	
	//Current generation of the population
	private int generation;
	
	//Array of all the fitness of the population
	private ArrayList<Integer> allFitness;
	
	

	
	/**
	 * Constructor
	 * @param size_population
	 * @param file
	 */
	public Population(int size_population, String file) {
		
		//Create the array of the population
		popu = new ArrayList();
		
		//Read the file and obtain the data
		flows = QAPReader.readFlows(file);
		distances = QAPReader.readDistances(file);
		fileSize = QAPReader.readSize(file);
		
		//Create the array of the fitness
		allFitness = new ArrayList();
	
		
		//Add the Chromosomes to the population
		for( int i = 0; i < size_population; i++)
			popu.add(new Chromosome(fileSize));
		
		//The generation starts in 0
		this.generation = 0;
		//Initialize all the chromosomes
		this.initialize();
		
	}
	
	/**
	 * Initialize all the chromosomes using random numbers
	 */
	private void initialize() {
		
		for( Chromosome c: popu)
			c.initialize();
				
	}
	
	/**
	 * Get the size of the population
	 * @return
	 */
	public int getSize() {
		return popu.size();
	}
	
	/**
	 * Get the current generation of the population
	 * @return
	 */
	public int getGeneration() {
		return generation;
	}
	
	
	/**
	 * Get the array of chromosomes of the population
	 * @return
	 */
	public ArrayList<Chromosome> getPopulation(){
		return popu;
	}
	
	@Override
	public String toString() {
		
		String str = "";
		
		for(Chromosome c: popu)
			str = str.concat(c.toString() + "\n");
		
		return str;
	}
	
	/**
	 * Calculate the fitness of a chromosome
	 * @param chromo_index the index of the chromosome
	 * @return int the fitness of the chromosome
	 */
	public int calculateFitness(int chromo_index) {
		int fitness = 0;
		
		if(chromo_index >= 0 && chromo_index < popu.size()) {
			Chromosome c = popu.get(chromo_index);
			
			for( int i = 0; i < this.fileSize; i++) {
				for( int j = 0; j < this.fileSize; j++) {
					fitness += flows.get(fileSize*i+j) * distances.get(fileSize*c.getGen(i)+c.getGen(j));
				}
			}
			
		}
		return -fitness;
	}
	/**
	 * Calculate the fitness of a chromosome
	 * @param c Chromosome to calculate its fitness
	 * @return the fitness of the chromosome
	 */
	public int calculateFitness(Chromosome c) {
		
		int fitness = 0;
			
		for( int i = 0; i < this.fileSize; i++) {
			for( int j = 0; j < this.fileSize; j++) {
				fitness += flows.get(fileSize*i+j) * distances.get(fileSize*c.getGen(i)+c.getGen(j));
			}
		}
			
		
		return -fitness;
	}
	
	/**
	 * Static function to calculate the fitness of a chromosome outside this class
	 * @param c
	 * @param flows
	 * @param distances
	 * @param fileSize
	 * @return the fitness
	 */
	public static int calculateFitness(Chromosome c, ArrayList<Integer> flows, ArrayList<Integer> distances, int fileSize ) {
		
		int fitness = 0;
		
		for( int i = 0; i < fileSize; i++) {
			for( int j = 0; j < fileSize; j++) {
				fitness += flows.get(fileSize*i+j) * distances.get(fileSize*c.getGen(i)+c.getGen(j));
			}
		}
			
		return -fitness;	
	}
	
	/**
	 * Calculate all the fitness of the population and save it into an array using the 
	 * baldwinian variant. This means that the fitness of the chromosome is the fitness
	 * of the chromosome after finding a better version of it using a greedy algorithm
	 */
	public void calculateAllFitnessBaldwinian() {
		ArrayList<Integer>new_fitness = new ArrayList();
		for(Chromosome c: popu) {
			Chromosome greedied = greedy.Algorithms.transposition(c, flows, distances, fileSize);
			new_fitness.add(calculateFitness(greedied));
		}
		allFitness = new_fitness;
	}
	
	/**
	 * Calculate all the fitness of the population and save it into an array using the current genes
	 */
	public void calculateAllFitnessGeneric() {
		ArrayList<Integer>new_fitness = new ArrayList();
		for(Chromosome c: popu) {
			new_fitness.add(calculateFitness(c));
		}
		allFitness = new_fitness;
	}
	
	/**
	 * Returns the termination condition
	 * @return true if the generation is bigger than a number
	 */
	public boolean terminationCondition() {
		return generation > 1000;
	}
	
	
	/**
	 * Select the best chromosomes of the population
	 * @param percentage percentage of the population that will be selected
	 */
	public void selection(double percentage) {
		
		int best_chromosome = 0;
		
		//Calculate the highest and lowest fitness to set the probabilities of been selected
		//The lowest fitness will have a probability of 0.1 of been selected and the highest will have 1.0
		int highestFitness = Integer.MIN_VALUE;
		int lowestFitness = Integer.MAX_VALUE;
		
		for(int i = 0; i < allFitness.size(); i++) {
			int fit = allFitness.get(i);
			
			if(fit > highestFitness) {
				highestFitness = fit;
				best_chromosome = i;
			}
			if(fit < lowestFitness)
				lowestFitness = fit;
		}
		
		
		
		
		ArrayList<Chromosome> new_popu = new ArrayList();
		ArrayList<Integer> new_fitness = new ArrayList();
		
		double new_popu_size = popu.size()*percentage;
		//Elitism
		new_popu.add(popu.get(best_chromosome));
		new_fitness.add(allFitness.get(best_chromosome));
		
		popu.remove(best_chromosome);
		allFitness.remove(best_chromosome);

		while(new_popu.size() < new_popu_size) {
			
			for( int i = 0; i < popu.size() && new_popu.size() < new_popu_size; i++) {
				//Formula to calculate the probability of been selected
				double prob = MyMath.linearInterpolation(lowestFitness, 0.1, highestFitness, 1, allFitness.get(i));
				double rand = ThreadLocalRandom.current().nextInt(0, 11) /10.0;

				if( rand <= prob ) {
					new_popu.add(popu.get(i));
					new_fitness.add(allFitness.get(i));
					//popu.remove(i);
					//allFitness.remove(i);
				}
			}			
		}
		popu = new_popu;
		allFitness = new_fitness;
		
	}
	
	/**
	 * Create new chromosomes until there is as much as the parameter size.
	 * It uses a linear function to calculate the probability of being copied in the population
	 * @param size
	 */
	public void replacement(int size) {
		
		ArrayList<Chromosome> new_popu = new ArrayList(popu);
		ArrayList<Integer> new_fitness = new ArrayList(allFitness);
		
		//Calculate the highest and lowest fitness to set the probabilities have descendants
		//The lowest fitness will have a probability of 0.1 of having descendants and the highest will have 1.0
		int highestFitness = Integer.MIN_VALUE;
		int lowestFitness = Integer.MAX_VALUE;
		

		for(int i = 0; i < popu.size(); i++) {
			int fit = allFitness.get(i);
			
			if(fit > highestFitness)
				highestFitness = fit;
			if(fit < lowestFitness)
				lowestFitness = fit;
		}

		while(new_popu.size() < size) {
			for(int i = 0; i < popu.size() && new_popu.size()<size; i++) {
				
				//Formula to calculate the probability of having descendants
				double prob = MyMath.linearInterpolation(lowestFitness, 0.1, highestFitness, 0.7, allFitness.get(i));
				//System.out.println(lowestFitness + "  " + highestFitness + allFitness.get(i));
				double rand = ThreadLocalRandom.current().nextInt(0, 11) /10.0;
				if( rand <= prob ) {
					//Add a new chromosome to the population
					new_popu.add(new Chromosome(popu.get(i)));
					new_fitness.add(allFitness.get(i));
				}
				
			}
		}

		popu = new_popu;
		allFitness = new_fitness;
		
		this.generation++;
	}
	
	/**
	 * Mutate the population
	 * It uses a linear function to calculate the probability of being mutated
	 */
	public void mutation() {
		
		//Calculate the highest and lowest fitness to set the probabilities to mutate
		//The lowest fitness will have a probability of 0.1 of mutating and the highest will have 1.0
		int highestFitness = Integer.MIN_VALUE;
		int lowestFitness = Integer.MAX_VALUE;
		
		
		for(int i = 0; i < popu.size(); i++) {
			int fit = allFitness.get(i);
			
			if(fit > highestFitness)
				highestFitness = fit;
			if(fit < lowestFitness)
				lowestFitness = fit;
		}
		
		for(int i = 0; i < popu.size(); i++) {
			//Formula to calculate the probability of mutating
			double prob = 1 - MyMath.linearInterpolation(lowestFitness, 0.1, highestFitness, 1.0, allFitness.get(i));
			popu.get(i).mutate(prob);
		}
		

	}
	
	/**
	 * Uses a greedy algorithm to learn
	 */
	public void learn() {
		
		for( int i = 0; i < popu.size(); i++) {
			Chromosome c = greedy.Algorithms.transposition(popu.get(i), flows, distances, fileSize);
			popu.set(i, c);
		}
		
		
	}
	
	/**
	 * Returns the chromosome with the best fitness of the population
	 * @return Chromosome
	 */
	
	public Chromosome getBestChromosome() {
		
		Chromosome best = popu.get(0);
		int best_fitness = allFitness.get(0);
		for( int i = 1; i < popu.size(); i++) {
			
			int fit = allFitness.get(i);
			if(fit > best_fitness) {
				best_fitness = fit;
				best = popu.get(i);
			}
			
		}
				
		return best;
	}
	
	public boolean testGreedyConstructive() {
		
		Chromosome c = greedy.Algorithms.constructive(flows, distances, fileSize);
		boolean correcto = true;
		for( int i= 0; i < c.getSize() && correcto; i++)
			correcto = c.getChromosome().contains((Integer)i);
		
		
		System.out.println("Size -> " + c.getSize());
		System.out.println("Fitness -> " + this.calculateFitness(c));
		System.out.println("Correcto -> " + correcto);
		System.out.println("Cromosoma -> " + c);
		return correcto;
	}
	
	public int testGreedyTransposition() {
		Chromosome c = new Chromosome(fileSize);
		c.initialize();
		
		System.out.println("Fitness c -> " + calculateFitness(c));
		
		Chromosome better = greedy.Algorithms.transposition(c, flows, distances, fileSize);
		
		System.out.println("Fitness better -> " + calculateFitness(better));

		
		return 0;
	}
	
}
