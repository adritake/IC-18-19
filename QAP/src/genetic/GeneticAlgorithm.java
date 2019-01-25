package genetic;

import java.util.ArrayList;

import org.jfree.ui.RefineryUtilities;

import utils.Plotter;

public class GeneticAlgorithm {

	
	/**
	 * Start the generic genetic algorith with any variant
	 * @param data
	 * @param population_size
	 * @param selection_percentage
	 * @param replacement_size
	 * @return The best chromosome obtained
	 */
	public static Chromosome startGeneric(String data, int population_size, float selection_percentage, int replacement_size) {
	
		//Array of the best fitness generated in each population
		ArrayList<Integer> generatedFitness = new ArrayList();
		
		Chromosome best = null;
		//Initial population
		Population p = new Population(population_size, data );
		//While not termination
		while(!p.terminationCondition()) {
			//Calculate all the Fitness of the population
			p.calculateAllFitnessGeneric();
			System.out.println("GENERATION " + p.getGeneration() + "/1000  ------------------------------------------");
			//Population = Select population
			p.selection(selection_percentage);
			//Population = Mutate population
			p.mutation();
			//Calculate again all the fitness for the mutated chromosomes
			p.calculateAllFitnessGeneric();
			//Population = replace population
			p.replacement(replacement_size);
			//Evaluate population
			best = p.getBestChromosome();
			//Add the best fitness to the array
			generatedFitness.add(-p.calculateFitness(best));
			System.out.println("Best fitness = " + p.calculateFitness(best) );
			
		}
		
		Plotter plotter = new Plotter("Fitness variation");
		plotter.insertData(generatedFitness);
		plotter.pack();
	    RefineryUtilities.centerFrameOnScreen(plotter);
	    plotter.setVisible(true);
		return best;

		
	}
	
	/**
	 * Starts the Baldwinian variant of the genetic algorithm
	 * @param data
	 * @param population_size
	 * @param selection_percentage
	 * @param replacement_size
	 * @return the best chromosome obtained
	 */
	public static Chromosome startBaldwinian(String data, int population_size, float selection_percentage, int replacement_size) {
		
		//Array of the best fitness generated in each population
		ArrayList<Integer> generatedFitness = new ArrayList();
		
		Chromosome best = null;
		//Initial population
		Population p = new Population(population_size, data );
		
		//While not termination
		while(!p.terminationCondition()) {
			//Calculate all the fitness of the population using the local optimum of the genes
			p.calculateAllFitnessBaldwinian();
			System.out.println("GENERATION " + p.getGeneration() + "/1000  ------------------------------------------");
			//Population = Select population
			p.selection(selection_percentage);
			//Population = Mutate population
			p.mutation();
			//Calculate again all the fitness of the population using the local optimum of the genes
			p.calculateAllFitnessBaldwinian();
			//Population = replace population
			p.replacement(replacement_size);
			//Evaluate population
			best = p.getBestChromosome();
			//Add the best fitness to the array
			generatedFitness.add(-p.calculateFitness(best));
			System.out.println("Best fitness = " + p.calculateFitness(best) );
			
		}
		Plotter plotter = new Plotter("Fitness variation");
		plotter.insertData(generatedFitness);
		plotter.pack();
	    RefineryUtilities.centerFrameOnScreen(plotter);
	    plotter.setVisible(true);
		
		return best;

		
	}
	
	/**
	 * Start the Lamarckian variant of the genetic algorithm
	 * @param data
	 * @param population_size
	 * @param selection_percentage
	 * @param replacement_size
	 * @return the best chromosome obtained
	 */
	public static Chromosome startLamarckian(String data, int population_size, float selection_percentage, int replacement_size) {
		
		//Array of the best fitness generated in each population
		ArrayList<Integer> generatedFitness = new ArrayList();
		
		Chromosome best = null;
		//Initial population
		Population p = new Population(population_size, data );
		
		//While not termination
		while(!p.terminationCondition()) {
			//Use a greedy algorithm to improve the genes
			p.learn();
			//Calculate all the fitness of the population
			p.calculateAllFitnessGeneric();
			System.out.println("GENERATION " + p.getGeneration() + "/1000  ------------------------------------------");
			//Population = Select population
			p.selection(selection_percentage);
			//Population = Mutate population
			p.mutation();
			//Calculate again all the fitness of the population
			p.calculateAllFitnessGeneric();
			//Population = replace population
			p.replacement(replacement_size);
			//Evaluate population
			best = p.getBestChromosome();
			//Add the best fitness to the array
			generatedFitness.add(-p.calculateFitness(best));
			System.out.println("Best fitness = " + p.calculateFitness(best) );
			
		}
		
		Plotter plotter = new Plotter("Fitness variation");
		plotter.insertData(generatedFitness);
		plotter.pack();
	    RefineryUtilities.centerFrameOnScreen(plotter);
	    plotter.setVisible(true);
		
		return best;

		
	}
	
	
}
