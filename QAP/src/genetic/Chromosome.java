package genetic;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class that represents the chromosome in the genetic algorithm
 * 
 * @author Adrián
 *
 */
public class Chromosome{
	
	//Array of genes
	private ArrayList<Integer> chromo;
	//Size of the array, only used when we want to create an empty chromosome with that size
	private int size;
	
	/**
	 * Constructor for creating an empty chromosome. The method initialize() should
	 * be called after using this constructor
	 * @param size Size of the problem (number of factories)
	 */
	public Chromosome(int size) {
		chromo = new ArrayList<Integer>();
		this.size = size;
	}
	
	/**
	 * Constructor with an ArrayList<Integer> as parameter
	 * @param chr
	 */
	public Chromosome(ArrayList<Integer> chr) {
		chromo = new ArrayList<Integer>(chr);
		size = chromo.size();
	}
	
	/**
	 * Copy constructor
	 * @param c
	 */
	public Chromosome(Chromosome c) {
		chromo = new ArrayList<Integer>(c.getChromosome());
		size = chromo.size();
	}
	
	/**
	 * Constructor with an int [] as parameter
	 * @param arr
	 */
	public Chromosome(int [] arr) {
		chromo = new ArrayList<Integer>();
		this.size = arr.length;
		for( int i = 0; i < this.size; i++)
			chromo.add(arr[i]);
	}
	
	/**
	 * Get the size of the chromosome (number of genes)
	 * @return
	 */
	public int getSize() {
		return chromo.size();
	}
	
	/**
	 * Get a gen of the chromosome
	 * @param gen_index
	 * @return gen , -1 if the index is wrong
	 */
	public Integer getGen(int gen_index) {
		Integer gen = -1;
		if(gen_index >= 0 && gen_index < chromo.size())
			gen = chromo.get(gen_index);
		return gen;
	}
	
	/**
	 * Initialize the genes of the chromosome with a random number between 0 and the size of the problem
	 * One number appears only once in the chromosome
	 */
	public void initialize () {
		
		ArrayList<Integer> initialValues = new ArrayList();
		while( initialValues.size() < size ) {
			
			Integer randomNum = ThreadLocalRandom.current().nextInt(0, size);
			if(!initialValues.contains(randomNum))
				initialValues.add(randomNum);
			
		}
		chromo = initialValues;
		
	}
	
	/**
	 * Gets the array of genes
	 * @return ArrayList<Integer>
	 */
	public ArrayList<Integer> getChromosome(){
		return chromo;
	}
	
	@Override
	public String toString() {
		
		String str = "";
		
		for( Integer i: chromo)
			str = str.concat(i + " ");
		
		return str;
	}
	
	/**
	 * Mutate the chromosome. Randomly swaps two genes.
	 * @param mutationProbability number in the range [0,1]. 
	 */
	
	public void mutate( double mutationProbability ) {
		
		mutationProbability = 1 - mutationProbability;
		int n_mutations = chromo.size()/5;
		n_mutations = ThreadLocalRandom.current().nextInt(0, n_mutations+1);
		
		double m = ThreadLocalRandom.current().nextInt(0, 11);
		
		for(int i = 0; i < n_mutations; i++)
			if( (m/10) >= mutationProbability ) {
				Integer randomNum1 = ThreadLocalRandom.current().nextInt(0, chromo.size());
				Integer randomNum2 = ThreadLocalRandom.current().nextInt(0, chromo.size());
				Integer gen1 = chromo.get(randomNum1);
				Integer gen2 = chromo.get(randomNum2);
				
				chromo.set(randomNum1, gen2);
				chromo.set(randomNum2, gen1);
			}
	}

		

}
