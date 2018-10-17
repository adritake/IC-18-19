package practica1;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * Clase encargada de crear la red neuronal y cambiar sus valores a partir
 * del conjunto de datos de entrada
 * 
 * @author Adrián
 *
 */
public class Entrenador {
	
	private ArrayList<Neurona> neuronas;
	private int [][][] imagenes;
	private int [] labels;
	
	/**
	 * Constructor de la clase
	 */
	public Entrenador() {
		
		neuronas = new ArrayList<>();
		
		for( int i = 0; i < 10; i ++) 
			neuronas.add( new Neurona(i,2000) );
		
		imagenes = null;
		labels = null;
			
	
	}
	
	
	public void ComenzarEntrenamiento(String pathImagenes, String pathLabels) {
		
		//Lectura de las imagenes
		try {
			imagenes = MNISTDatabase.readImages(pathImagenes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Lectura de las etiquetas de entrenamiento
	
		try {
			labels = MNISTDatabase.readLabels(pathLabels);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int tam_datos = labels.length;
		
		/*
		for(Neurona n: neuronas){
			
			if( n.Evalua(imagenes[0]) )
				System.out.println("Neurona " + String.valueOf(n.getNombre()) + " se ha activado");
			else
				System.out.println("Neurona " + String.valueOf(n.getNombre()) + " NO se ha activado");
				
				
		}
		*/
		
		int n_errores = 0;
		int prediccion = 0;
		
		for( int i = 0; i < tam_datos; i++) {
			
			for(Neurona n: neuronas) {
				
				boolean activa = n.Evalua(imagenes[i]);
				
				if(activa) {
					if(n.getNombre() != labels[i]) {
						n.variaPesos(imagenes[i], false);
					}
					prediccion = n.getNombre();
					
				}
				else {
					if(n.getNombre() == labels[i]) {
						n.variaPesos(imagenes[i], true);
										}
				}
						
			}
			
			if(prediccion != labels[i]) n_errores++;
			//System.out.println("La red neuronal dice que se trata de un " + String.valueOf(prediccion) + " y es un " + String.valueOf(labels[i]));
			
		}
		
		

		
		System.out.println("NUMERO DE ERRORES: " + String.valueOf(n_errores));
		System.out.println("PORCENTAJE DE ERROR: " + String.valueOf((n_errores/(tam_datos*1.0))*100));
		
	}
	
	public void ComenzarTest(String pathImagenes, String pathLabels) {
		
		//Lectura de las imagenes
		try {
			imagenes = MNISTDatabase.readImages(pathImagenes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Lectura de las etiquetas de entrenamiento
	
		try {
			labels = MNISTDatabase.readLabels(pathLabels);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int tam_datos = labels.length;
		
		int prediccion = 0;
		int n_errores = 0;
		int n_activadas = 0;
		
		for( int i = 0; i < 10000; i++) {
			
			n_activadas = 0;
			for(Neurona n: neuronas) {
				
				if(n.Evalua(imagenes[i])) {
					prediccion = n.getNombre();
					n_activadas ++;
				}
						
			}
			
			if(prediccion != labels[i]) n_errores++;
			System.out.println("Iteracion: " + i + ". La red neuronal dice que se trata de un " + prediccion + " y es un " + labels[i] + ". Neuronas activadas: " + n_activadas);
			
		}
		
		System.out.println("NUMERO DE ERRORES: " + String.valueOf(n_errores));
		System.out.println("PORCENTAJE DE ERROR: " + String.valueOf((n_errores/(10000*1.0))*100));
			
	}
	
	
	
	

}
