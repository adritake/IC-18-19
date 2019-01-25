package QAP;

import java.util.ArrayList;
import java.util.Arrays;

import org.jfree.ui.RefineryUtilities;

import genetic.Chromosome;
import genetic.GeneticAlgorithm;
import genetic.Population;
import utils.MyMath;
import utils.Plotter;
import utils.QAPReader;

public class Main {

	public static void main(String[] args) {
		
		/*
		Population p = new Population(0,"C:\\Users\\atr_9\\eclipse-workspace\\QAP\\data\\tai256c.dat");
		p.testGreedyTransposition();
		*/
		long startTime = System.nanoTime();    
		String file = "C:\\Users\\atr_9\\eclipse-workspace\\QAP\\data\\tai256c.dat";
		Chromosome best = GeneticAlgorithm.startGeneric(file, 100, (float) 0.8, 100);
		/*
		ArrayList<Integer> flows = QAPReader.readFlows(file);
		ArrayList<Integer> distances = QAPReader.readDistances(file);
		int fileSize = QAPReader.readSize(file);

		Chromosome best = greedy.Algorithms.constructive(flows, distances, fileSize);
		*/
		long estimatedTime = System.nanoTime() - startTime;

		System.out.println ("The best chromosome is: " + best.toString() );
		//System.out.println("Fitness = " + Population.calculateFitness(best, flows, distances, fileSize));
		System.out.println("Tiempo -> " + estimatedTime/1000000000.0);
		
		
		
	}

}
