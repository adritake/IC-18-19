package practica1;

/**
 * Clase Neurona encargada de recibir las entradas de la imágen y activarse
 * si se corresponde al número encargado de reconocer
 * @author Adrián
 *
 */
public class Neurona {

	//Variables privadas
	private double [] pesosEntrada;
	private boolean activada;
	private int nombre;
	private final int TAM_ENTRADAS = 28*28 +1;
	private final int TAM_MATRIZ = 28;
	
	/**
	 * Constructor
	 * @param nombre Nombre de la neurona
	 * @param umbral umbral para la entrada cero
	 */
	public Neurona(int nombre, double umbral) {
		
		pesosEntrada = new double [TAM_ENTRADAS];
		
		//Valor del umbral
		pesosEntrada[0] = umbral;
		
		//Actualizamos los valores de los pesos de forma aleatoria
		for(int i = 1; i < TAM_ENTRADAS; i++) {
			pesosEntrada[i] = (float) (Math.random() * 100) + 1;
		}
		
		activada = false;
		this.nombre = nombre;
		
	}
	
	/**
	 * Obtiene el nombre de la neurona
	 * @return nombre: String
	 */
	int getNombre() {
		return nombre;
	}
	
	boolean getActivada() {
		return activada;
	}
	
	double getPeso ( int entrada ) {
		
		if(entrada >= 1 && entrada < TAM_ENTRADAS) {
			return pesosEntrada[entrada];
		}
		else
			return Float.MAX_VALUE;
		
	}
	
	void setPeso ( int entrada, float valor ) {
		
		if(entrada >= 1 && entrada < TAM_ENTRADAS) {
			pesosEntrada[entrada] = valor;
		}
		
	}
	
	void sumaPeso ( int entrada, double valor ) {
		
		if(entrada >= 1 && entrada < TAM_ENTRADAS) {
			pesosEntrada[entrada] = pesosEntrada[entrada] + valor;
		}
		
	}
	
	double [] getPesos() {
		return pesosEntrada;
	}
	
	void setPesos(double new_pesos[]) {
		if(new_pesos.length == TAM_ENTRADAS - 1)
			for(int i = 0; i < TAM_ENTRADAS; i++)
				pesosEntrada[i+1]=new_pesos[i];
	}
	
	void variaPesos( int valor[][],boolean suma) {
		
		int factor = (suma)? 1 : -1;
		
		if(valor.length == TAM_MATRIZ) {
			
			int indice = 1;
			for(int i = 0; i < TAM_MATRIZ; i++) {
				for(int j = 0; j < TAM_MATRIZ; j++) {
					sumaPeso(indice, valor[i][j]*factor);
					indice ++;
				}
			}
		}
				
	}
	
	boolean Evalua(int[][]entrada) {
		
		double sum = pesosEntrada[0];
		int indice = 1;
		
		for(int i = 0; i < TAM_MATRIZ; i++) {
			for(int j = 0; j < TAM_MATRIZ; j++) {
				sum += entrada[i][j] * pesosEntrada[indice];
				indice ++;
			}
		}
		
		activada = (sum>0)? true : false;
		//System.out.println("Evaluación de la neurona " + String.valueOf(nombre) + " = " + String.valueOf(sum));
		return activada;
	}
	
	
	
}
