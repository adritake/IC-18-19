package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Functions to read the data
 * @author Adrián
 */

public class QAPReader {

	
	/**
	 * Read the matrix of flows
	 * @param file
	 * @return ArrayList<Integer> with the flows
	 */
	public static ArrayList<Integer> readFlows(String file){
		
		int size = 0;
		ArrayList<Integer> flows = new ArrayList();
		BufferedReader br;

		try {
			
			br = new BufferedReader(new FileReader(file));
			//Read the size of the matrix
			size = Integer.parseInt(br.readLine().trim());
			
			//Empty line, 
			br.readLine();
			
			//For each line we read element by element skipping the white spaces and adding concating the numbers
			for(int i = 0; i < size; i++) {
				String line = br.readLine();
				line = line.trim().replaceAll(" +", " ");
				String [] columns = line.split(" ");
				for(int j = 0; j < size; j++)
					flows.add(Integer.parseInt(columns[j]));
				
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		
		
		return flows;
	}
	
	/**
	 * Read the matrix of distances
	 * @param file
	 * @return
	 */
	public static ArrayList<Integer> readDistances( String file ){
		
		int size = 0;
		ArrayList<Integer> distances = new ArrayList();
		BufferedReader br;

		try {
			
			br = new BufferedReader(new FileReader(file));
			//Read the size of the matrix
			size = Integer.parseInt(br.readLine().trim());
			
			//Empty line
			br.readLine();
			
			//Skip the first matrix
			for(int i = 0; i < size; i++)
				br.readLine();
				
			//Empty line
			br.readLine();
			
			//For each line we split it in columns and add the elements to the array
			for(int i = 0; i < size; i++) {
				String line = br.readLine();
				line = line.trim().replaceAll(" +", " ");
				String [] columns = line.split(" ");
				for(int j = 0; j < size; j++)
					distances.add(Integer.parseInt(columns[j]));
				
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		
		
		return distances;
		
	}
	
	
	
	/**
	 * Read the size of the matrix
	 * @param file
	 * @return Integer size
	 */
	public static int readSize(String file) {
		
		int size = 0;
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			br.close();
			
			line = line.trim();
			
			size = Integer.parseInt(line);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return size;
		
	}
	
}
