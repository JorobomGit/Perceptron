package clasificadores;

import datos.Datos;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClasificadorRegrLogistica extends Clasificador {
    
    Double[] w = null; //Vector de coeficientes de regresion
    
    ArrayList<String> clasesDisponibles = new ArrayList<>();
    
    int epocas = -1;
    
    @Override
    public void entrenamiento(Datos datostrain) {
        Random rand = new Random();
        //w tendra el tamano del numero de atributos
        if(w==null){
            w = new Double[datostrain.getNumAtributos()];
            //inicializamos w0 a (-0.5,0.5)
            for (int i=0;i<w.length;i++) {
                w[i] = -1 + (1 + 1) * rand.nextDouble();
                //System.out.println("Random: " + w[i]);
            }
        }
        
        Double[] vectorx = new Double[datostrain.getNumAtributos()];
        Double x; //Exponente de x
        Double y; //sigmoidal
               
        
        if(epocas==-1){
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            try {
                System.out.println("Introduzca epocas para Reg. Logistica:");
                epocas = Integer.parseInt((bufferRead.readLine()));
            } catch (IOException ex) {
                Logger.getLogger(ClasificadorRegrLogistica.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        
        for(int ie=0; ie<epocas;ie++){
            //Para cada dato corregimos coeficientes
            for(int i=0; i<datostrain.getNumDatos();i++){
                x=0.0;
                vectorx[0] = 1.0;
                //Hasta size-1 para evitar la clase
                for(int j=0;j<datostrain.getNumAtributos();j++){
                    if(j==(datostrain.getNumAtributos())){
                       //En size-1 guardamos clase para obtener Tn
                       if(!clasesDisponibles.contains(datostrain.getDatos()[i][j]))
                           clasesDisponibles.add(datostrain.getDatos()[i][j]);
                    }else
                       vectorx[j+1]=Double.parseDouble(datostrain.getDatos()[i][j]);
                }

                //x = vectorx * traspuesta(w)
                //Producto escalar u*v = u1*v1+u2*v2...
                for(int j=0;j<vectorx.length;j++)
                    x = x + (vectorx[j]*w[j]);


                y=1/(1+Math.pow(Math.E,-x));
                //Hasta aqui esta calculada la sigmoidal


                //Tn es 0 o 1 dependiendo de la clase
                
                double K=0.1; //Constante

                //Correccion de w
                for(int j=0;j<vectorx.length;j++)
                    w[j] = w[j] - (vectorx[j]*K*(y-clasesDisponibles.indexOf(datostrain.getDatos()[i][datostrain.getNumAtributos()])));

                //AL FINAL DE ESTE METODO, DEBERIAMOS TENER UNA W CORREGIDA            
            }
        }

    }
    
    @Override
    public ArrayList<String> clasifica(Datos datos) {
        ArrayList<String> datosClasificados = new ArrayList<>();
        Double[] vectorx = new Double[datos.getNumAtributos()];
        Double x; //Exponente de x
        Double y; //sigmoidal
        
        //Realizamos sigmoidal
        //Producto escalar u*v = u1*v1+u2*v2...
                //Para cada dato corregimos coeficientes
        for(int i=0; i<datos.getNumDatos();i++){
            x=0.0;
            vectorx[0]=1.0;
            //Hasta size-1 para evitar la clase
            for(int j=0;j<datos.getNumAtributos();j++)
               vectorx[j+1]=Double.parseDouble(datos.getDatos()[i][j]);
            for(int j=0;j<vectorx.length;j++)
               x = x + (vectorx[j]*w[j]);
            y=1/(1+Math.pow(Math.E,-x));
            if(y>=0.5) //clase 1
                datosClasificados.add(this.clasesDisponibles.get(1)); //Clase 1
            else
                datosClasificados.add(this.clasesDisponibles.get(0)); //Clase 2
        }

        //Reinicializamos variables globales para no acumular en cruzada.
        w = null;
        clasesDisponibles = new ArrayList<>();    
        
        return datosClasificados;
    }
}
