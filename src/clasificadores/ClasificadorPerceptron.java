package clasificadores;

import datos.Datos;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClasificadorPerceptron extends Clasificador {

    ArrayList<String> clasesDisponibles = new ArrayList<>();

    int epocas;

    double b;
    double[] w; //Vector de pesos w
    double tasa_aprendizaje;

    public ClasificadorPerceptron(int tam_w) {
        //Inicializar todos los pesos y sesgos (por simplicidad a cero)
        this.b = 0.0;
        this.w = new double[tam_w];
        for (int i = 0; i < tam_w; i++) {
            this.w[i] = 0.0;
        }

        //Establecer la tasa de aprendizaje a (0 < a ≤1)
        this.tasa_aprendizaje = 1;
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
        } catch (IOException ex) {
            Logger.getLogger(ClasificadorPerceptron.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Variable xi equivalente a las activaciones si
        double[] xi = new double[datostrain.getNumAtributos()];

        double y_in, sumatorio;
        int y;

        double umbral = 0;
        while (epocas_aux < this.epocas) {
            System.out.println("x1 x2 b      y_in y t        Dw1 Dw2 Db      w1 w2 b");
            double b_aux = b;
            double[] w_aux = w.clone();
            //Paso 2: Para cada par de entrenamiento (s:t), ejecutar los pasos 3-5:
            for (int i = 0; i < datostrain.getDatos().length; i++) {
                sumatorio = 0;

                //Paso 3: Establecer las activaciones a las neuronas de entrada xi = si (i=1…n)
                for (int j = 0; j < datostrain.getNumAtributos(); j++) {
                    xi[j]= Double.parseDouble(datostrain.getDatos()[i][j]);
                }
                //Paso 4: Calcular la respuesta de la neurona de salida:
                //y _ in = b + sumatorio(x, w)
                for (int j = 0; j < w.length; j++) {
                    sumatorio += xi[j] * w[j];
                }

                y_in = b + sumatorio;

                //Para actualizar los pesos necesitamos la t, hay que configurar la t por clases
                if (y_in >= umbral) {
                    y = 1;
                } else {
                    y = -1;
                }

                double t;

                String aux_clases = "";
                /*Obtenemos las distintas clases*/
                for (int l = 0; l < datostrain.getNumClases(); l++) {
                    aux_clases = aux_clases.concat(datostrain.getDatos()[i][datostrain.getNumAtributos() + l]);
                }

                //Paso 5: Ajustar los pesos y el sesgo si ha ocurrido un error para este patrón: 
                t = datostrain.getClases().get(aux_clases);

                if (y != t) {
                    for (int j = 0; j < w.length; j++) {
                        w[j] = w[j] + (this.tasa_aprendizaje * t * xi[j]);
                    }
                    b = b + (this.tasa_aprendizaje * t);
                }
                System.out.println(xi[0] + " " + xi[1] + " 0\t" 
                        + y_in +" "+ y +" "+ t +"\t"+ 
                        (this.tasa_aprendizaje * t * xi[0]) + " "+(this.tasa_aprendizaje * t * xi[1]) +" "+ (this.tasa_aprendizaje * t)
                +"\t"+ w[0]+" " + w[1] + " " + b);
            }
            epocas_aux++;
            //if no cambia, break
            if (this.comparador(w, w_aux) && b == b_aux) {
                break;
            }
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
            sumatorio = 0;
            //Paso 2: Establecer las activaciones a las neuronas de entrada xi = si (i=1…n)
            for (int j = 0; j < datos.getNumAtributos(); j++) {
                xi[j]= Double.parseDouble(datos.getDatos()[i][j]);
                //System.out.println("X: " + xi[j]);
            }
            
            
            //Paso 3: Calcular la respuesta de la neurona de salida:
            //y _ in = b + sumatorio(x, w)
            for (int j = 0; j < w.length; j++) {
                sumatorio += xi[j] * w[j];
            }

            y_in = b + sumatorio;

            //Calculamos respuesta de salida
			if(y_in>= umbral){
                y = 1;
                y = -1;
            }

            //Obtenemos todas las predicciones
            datosClasificados.add(Integer.toString(y));
        }

        return datosClasificados;
    }

    private boolean comparador(double[] x, double[] y) {
        for (int i = 0; i < x.length; i++) {
            if (x[i] != y[i]) {
                return false;
            }
        }

        return true;

    }

}
