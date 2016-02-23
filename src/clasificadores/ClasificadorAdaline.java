package clasificadores;
import datos.Datos;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClasificadorAdaline extends Clasificador {
	
    private ArrayList<String> clasesDisponibles = new ArrayList<>();

    private int epocas;

    private double b;
    private Double[] w; //Vector de pesos w
    private Double tasa_aprendizaje;

    public ClasificadorAdaline(int tam_w) {
        //Inicializar todos los pesos y sesgos (por simplicidad a cero)
        this.b = 1.0;
        this.w = new Double[tam_w];
        for (int i = 0; i < tam_w; i++) {
            this.w[i] = 0.0;
        }

        //Establecer la tasa de aprendizaje a (0 < a ≤1)
        this.tasa_aprendizaje = 0.1;
    }

    @Override
    public void entrenamiento(Datos datostrain) {
        //Paso 1: Mientras que la condición de parada sea falsa, ejecutar pasos 2-6
        //Condicion de parada: o maximo numero de epocas o los pesos no cambian
        int epocas_aux = 0;

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

        double y_in, sumatorio;
        int y;

        double umbral = 0;
        double errorCuadratico = 0;
        //Paso 1: Mientras la condicion de parada sea falsa ejecutar los pasos 2-6:
        while (epocas_aux < this.epocas) {
        	errorCuadratico=0;
            Double b_aux = b;
            Double[] w_aux = w.clone();
            //Paso 2: Para cada par de entrenamiento (s:t), ejecutar los pasos 3-5:
            for (int i = 0; i < datostrain.getDatos().length; i++) {
                sumatorio = 0;
                double error=0;
                //Paso 3: Establecer las activaciones a las neuronas de entrada xi = si (i=1…n)
                for (int j = 0; j < datostrain.getNumAtributos(); j++) {
                    xi[j] = Double.parseDouble(datostrain.getDatos()[i][j]);
                }
                //Paso 4: Calcular la respuesta de la neurona de salida:
                //y _ in = b + sumatorio(x, w)
                for (int j = 0; j < w.length; j++) {
                    sumatorio += xi[j] * w[j];
                }

                y_in = b + sumatorio;

                double t;

                String aux_clases = "";
                /*Obtenemos las distintas clases*/
                for (int l = 0; l < datostrain.getNumClases(); l++) {
                    aux_clases = aux_clases.concat(datostrain.getDatos()[i][datostrain.getNumAtributos() + l]);
                }

                //Paso 5: Ajustar los pesos y el sesgo si ha ocurrido un error para este patrón: 
                t = datostrain.getClases().get(aux_clases);

                for (int j = 0; j < w.length; j++) {
                	error = (t-y_in);
                    w[j] = w[j] + (this.tasa_aprendizaje * (error) * xi[j]);
                    error = Math.pow(error, 2);
                }
                b = b + (this.tasa_aprendizaje * (t-y_in));
                errorCuadratico += error;
            }
            System.out.println(epocas_aux + "\t" + errorCuadratico/datostrain.getNumDatos());
            epocas_aux++;
            //if no cambia, break
            if(this.comparador(w, w_aux) && Objects.equals(b, b_aux))
                break;
        }
        System.out.println("Numero de epocas realizadas: " + epocas_aux);

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
                sumatorio += xi[j] + w[j];
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
    
    private boolean comparador(Double[] x, Double[] y){
        for(int i=0;i<x.length;i++)
            if(x[i] != y[i]) return false;
        
        return true;
        
    }

}