package particionado;

import clasificadores.ClasificadorPerceptron;
import datos.Datos;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ValidacionSimple implements EstrategiaParticionado {
    private int numParticiones=100;
    
    @Override
    // Devuelve el nombre de la estrategia de particionado
    public String getNombreEstrategia() {
        return "Validación Simple";
    }
    
    @Override
    // Devuelve el numero de particiones
    public int getNumeroParticiones() {
        return numParticiones;
    }
    
    @Override
    // Crea particiones segun el metodo tradicional de división de los datos segun el
    // porcentaje deseado. Devuelve una sola partición con un conjunto de train y otro de test
    public ArrayList<Particion> crearParticiones(Datos datos) {
        ArrayList<Particion>    particiones = new ArrayList<>();
        ArrayList<Integer>      indicesTrain = new ArrayList<>();
        ArrayList<Integer>      indicesTest = new ArrayList<>();    
        /*Validacion simple divide en dos los datos segun un porcentaje (numParticiones)*/
        /*80% a train y 20% a test por ejemplo*/
        /*Cogemos datos.datos y sacamos indices para nuestra variable particiones*/

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Escriba porcentaje de entrenamiento (de 0 a 100):");
            System.out.println("NOTA: si escribe un numero <= 0 o >= 100, entrenamiento y test seran del mismo tamano. (1-1)");
            this.numParticiones = Integer.parseInt((bufferRead.readLine()));
        } catch (IOException ex) {
            Logger.getLogger(ValidacionSimple.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        for(int i =0; i<datos.getNumDatos();i++){
            Random rnd = new Random();
            int random = (int) (rnd.nextDouble()*datos.getNumDatos());            
            /*numParticiones representa el porcentaje*/
            if(i<(datos.getNumDatos()*numParticiones)/100){
                /*Anadimos en train*/
                while(indicesTrain.contains(random))
                    random = (int) (rnd.nextDouble()*datos.getNumDatos()); /*Repetimos valor hasta que encuentre uno sin asignar*/
                indicesTrain.add(random); /*Anadimos el numero*/        
            }else{
                /*Anadimos en test*/
                while(indicesTrain.contains(random) || indicesTest.contains(random))
                    random = (int) (rnd.nextDouble()*datos.getNumDatos()); /*Repetimos valor hasta que encuentre uno sin asignar*/
                indicesTest.add(random); /*Anadimos el numero*/
            }
        }
        if(this.numParticiones<=0 || this.numParticiones >= 100)
            particiones.add(new Particion(indicesTrain,indicesTrain));
        else
            particiones.add(new Particion(indicesTrain,indicesTest));
        
        return  particiones;
    }
}