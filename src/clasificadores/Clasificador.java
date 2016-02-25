package clasificadores;

import datos.Datos;
import java.io.IOException;
import java.util.ArrayList;
import particionado.EstrategiaParticionado;
import particionado.Particion;
import particionado.ValidacionSimple;

abstract public class Clasificador {

    //Métodos abstractos que se implementan en cada clasificador concreto

    abstract public void entrenamiento(Datos datosTrain);

    abstract public ArrayList<String> clasifica(Datos datosTest);

    // Obtiene el numero de aciertos y errores para calcular la tasa de fallo
    public double error(Datos datos, Clasificador clas) {
        ArrayList<String> clases = clas.clasifica(datos);
        //Aqui se compara con clases reales y se calcula el error
        
        //Obtenemos todas las clases de datos        
        ArrayList<String> clases_reales = new ArrayList<>();

        for(int i=0; i< datos.getNumDatos();i++)
            clases_reales.add(Integer.toString(datos.traducirLinea(datos.getDatos()[i])));
        
        double error=0;
        /*Obtenemos clases reales*/
        for(int i=0; i<datos.getNumDatos();i++){            
//            System.out.println("Dato" + i + ": " + datos.getDatos()[i][(datos.getDatos()[i].length)-1]);
//            System.out.println("clases" + i + ": " + clases.get(i));
            if(!clases.get(i).equals(clases_reales.get(i))){
                error++;
            }
        }

        return error/datos.getNumDatos();
    }

    // Realiza una clasificacion utilizando una estrategia de particionado determinada
    public static ArrayList<Double> validacion(EstrategiaParticionado part, Datos datos,
            Clasificador clas) {
        //Creamos las particiones siguiendo la estrategia llamando a datos.creaParticiones
        ArrayList<Particion> particiones = part.crearParticiones(datos);
        Datos train;
        Datos test;
        ArrayList<Double> errores = new ArrayList<>();
                           
        switch (part.getNombreEstrategia()) {
            case "Validación Simple":
                //Para validación porcentual entrenamos el clasf con la partición de train (extraerDatosTrain)
                train = datos.extraeDatosTrain(particiones.get(0));
                clas.entrenamiento(train);
                // y obtenemos el error en la particion test (extraerDatosTest)    
                test = datos.extraeDatosTest(particiones.get(0));
                errores.add(clas.error(test, clas));
                break;
        }
        return errores;
    }

    public static void main(String[] args) throws IOException {
        
        double media = 0;
        
        Datos d = Datos.cargaDeFichero(args[0]);
        
        /*******************/
        /*VALIDACION SIMPLE*/
        /*******************/
        
        EstrategiaParticionado partS = new ValidacionSimple();              
        
        System.out.println("****************************************");
        System.out.println("CLASIFICADOR PERCEPTRON");
        System.out.println("****************************************");
        Clasificador c = new ClasificadorPerceptron(d.getNumAtributos());      
              
        ArrayList<Double> errores = Clasificador.validacion(partS, d, c);
        
        media = errores.stream().map((err) -> err).reduce(media, (accumulator, _item) -> accumulator + _item);
        media = media/errores.size();
        System.out.println("Error medio Simple y Perceptron:" + media); // Se imprimen
        
        System.out.println("****************************************");
        System.out.println("CLASIFICADOR ADALINE");
        System.out.println("****************************************");
        c = new ClasificadorAdaline(d.getNumAtributos());      
              
        errores = Clasificador.validacion(partS, d, c);
        
        media = errores.stream().map((err) -> err).reduce(media, (accumulator, _item) -> accumulator + _item);
        media = media/errores.size();
        System.out.println("Error medio Simple y Perceptron:" + media); // Se imprimen
 
    }
}
