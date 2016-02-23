package clasificadores;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import datos.Datos;

public class ClasificadorAdaline extends Clasificador {

    private int epocas;

    private double b;
    private double[] w; //Vector de pesos w
    private double tasa_aprendizaje;
    private double tolerancia;

    public ClasificadorAdaline(int tam_w) {
        //Inicializar todos los pesos y sesgos (por simplicidad a cero)
        this.b = 1.0;
        this.w = new double[tam_w];
        for (int i = 0; i < tam_w; i++) {
            this.w[i] = 0.0;
        }
        this.tolerancia = 0.01;

        //Establecer la tasa de aprendizaje a (0 < a ≤1)
        this.tasa_aprendizaje = 0.01;
    }

    @Override
    public void entrenamiento(Datos datostrain) {
        //Paso 1: Mientras que la condición de parada sea falsa, ejecutar pasos 2-6
        //Condicion de parada: o maximo numero de epocas o los pesos no cambian

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Introduzca epocas para Perceptron:");
            this.epocas = Integer.parseInt((bufferRead.readLine()));
            System.out.println("Introduce la tasa de aprendizaje:");
            this.tasa_aprendizaje = Double.parseDouble(bufferRead.readLine());
        } catch (IOException ex) {
            Logger.getLogger(ClasificadorPerceptron.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Variable xi equivalente a las activaciones si
        double[] xi = new double[datostrain.getNumAtributos()];
        int epocas_aux;
        double[] w_aux = w.clone();
        double b_aux = b;
        for(epocas_aux = 0; epocas_aux< this.epocas; epocas_aux++){
        	for(int contador=0; contador < datostrain.getNumDatos(); contador++){
        		for(int index = 0; index < datostrain.getNumAtributos(); index++){
        			xi[index] = Double.parseDouble(datostrain.getDatos()[contador][index]);
        		}
        		double sumatorio = 0;
        		for(int index = 0; index < xi.length; index++){
        			sumatorio += xi[index] * w[index];
        		}
        		double y_in = b + sumatorio;
        		
        		double t = Double.parseDouble(datostrain.getDatos()[contador][datostrain.getNumAtributos() + 1]);
        		
        		for(int index=0; index < w.length; index++){
        			w_aux[index] = w_aux[index] + this.tasa_aprendizaje * (t-y_in) * xi[index];
        		}
        		
        		b_aux = b_aux + this.tasa_aprendizaje * (t-y_in);
        		
        	}
    		if(condicionParada(w_aux,b_aux)){
    			w = w_aux.clone();
    			break;
    		}
    		w = w_aux.clone();
        }
        System.out.println("Numero de epocas realizadas: " + epocas_aux);

    }
    
    private boolean condicionParada(double[] w, double b){
    	for(int index = 0; index<this.w.length;index++){
    		if(Math.abs((this.w[index] - w[index])) > this.tolerancia){
    			return false;
    		}
    	}
    	if(Math.abs((this.b - b)) > this.tolerancia){
			return false;
		}
    	return true;
    }

    @Override
    public ArrayList<String> clasifica(Datos datos) {
        ArrayList<String> datosClasificados = new ArrayList<>();
        //Paso 1: Para cada vector de entrada x a clasificar, ejecutar pasos 2-4
        //Variable xi equivalente a las activaciones si
        double[] xi = new double[datos.getNumAtributos()];
        double y_in, sumatorio;
        int y;

        double umbral = 0;
        for (int i = 0; i < datos.getNumDatos(); i++) {
            sumatorio=0;
            //Paso 2: Establecer las activaciones a las neuronas de entrada xi = si (i=1…n)
            for (int j = 0; j < datos.getNumAtributos(); j++) {
                xi[j] = Double.parseDouble(datos.getDatos()[i][j]);
            }
            //Paso 3: Calcular la respuesta de la neurona de salida:
            //y _ in = b + sumatorio(x, w)
            for (int j = 0; j < w.length; j++) {
                sumatorio += xi[j] * w[j];
            }

            y_in = b + sumatorio;

            //Calculamos respuesta de salida
            if (y_in >= umbral) {
                y = 1;
            } else{
                y = -1;
            }
            
            //Obtenemos todas las predicciones
            datosClasificados.add(Integer.toString(y));
               
        }

        return datosClasificados;
    }

}