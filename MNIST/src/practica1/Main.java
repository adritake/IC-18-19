package practica1;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {

		/*
		//Lectura de las imágenes de entrenamiento
		int [][][] imagenes = null;
		try {
			imagenes = MNISTDatabase.readImages("C:\\Users\\atr_9\\eclipse-workspace\\MNIST\\data\\train-images-idx3-ubyte.gz");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Cantidad de imágenes: " + String.valueOf(imagenes.length));

		/*
		//Codigo para pintar el primer número por la salida
		String line;
		for(int i = 0; i < 28; i++) {
			line = "";
			for(int j = 0; j < 28; j++) {
				line = line.concat(String.valueOf(imagenes[0][i][j]) + " ");
			}
			System.out.println(line);
		}
		*/



		/*
		//Lectura de las etiquetas de entrenamiento
		int [] labels = null;

		try {
			labels = MNISTDatabase.readLabels("C:\\Users\\atr_9\\eclipse-workspace\\MNIST\\data\\train-labels-idx1-ubyte.gz");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Cantidad de etiquetas: " + String.valueOf(labels.length));

		/*
		//Código para imprimir las diez primeras etiquetas
		for(int i = 0; i < 10; i++)
			System.out.println("Etiqueta " + i + ": " + String.valueOf(labels[i]));
		*/

		Entrenador entrenador = new Entrenador();

		System.out.println("Comienza el entrenamiento....");
		entrenador.ComenzarEntrenamiento("C:\\Users\\atr_9\\eclipse-workspace\\MNIST\\data\\train-images-idx3-ubyte.gz",
										 "C:\\Users\\atr_9\\eclipse-workspace\\MNIST\\data\\train-labels-idx1-ubyte.gz");


		System.out.println("Comienza el test....");
		entrenador.ComenzarTest("C:\\Users\\atr_9\\eclipse-workspace\\MNIST\\data\\t10k-images-idx3-ubyte.gz",
								"C:\\Users\\atr_9\\eclipse-workspace\\MNIST\\data\\t10k-labels-idx1-ubyte.gz");

	}

}
