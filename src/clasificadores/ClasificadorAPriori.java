package clasificadores;

import datos.Datos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClasificadorAPriori extends Clasificador {
    
    private String claseMayoritaria;

    public String getClaseMayoritaria() {
        return claseMayoritaria;
    }

    @Override
    public void entrenamiento(Datos datostrain) {
    // Busco la clase mayoritaria de los datos y la guardo
       /*La clase se encuentra en el ultimo elemento de cadas fila*/
        Map<String, Integer> clases = new HashMap<>();
        String datos[][] = datostrain.getDatos();
        for(int i=0;i<datostrain.getNumDatos();i++){
            if(clases.keySet().contains(datos[i][datos[i].length-1])){
                clases.put(datos[i][datos[i].length-1], clases.get(datos[i][datos[i].length-1])+1);
            }else{
                clases.put(datos[i][datos[i].length-1], 1);
            }
        }     

        //Bucle iterando todos los elementos del Map y obteniendo el que más se repite.
        int x=0;
        for (Map.Entry<String, Integer> entry : clases.entrySet())
        {
            if(entry.getValue()>x){
                this.claseMayoritaria=entry.getKey();
                x=entry.getValue();
            }
            //System.out.println(entry.getKey() + "/" + entry.getValue());
        }
        //System.out.println("Clase mayoritaria;" + this.claseMayoritaria);
    }

    @Override
    public ArrayList<String> clasifica(Datos datos) {
// Asigno la clase mayoritaria a todos los datos      
        ArrayList<String> datosClasificados = new ArrayList<>();
        for(int i=0;i<datos.getNumDatos();i++)
            datosClasificados.add(claseMayoritaria);
        return datosClasificados;
    }
}
