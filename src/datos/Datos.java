package datos;

import java.io.*;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import particionado.Particion;

public class Datos {

    int numDatos;
    int numAtributos;
    /*Primera fila del archivo, contiene el numero de filas*/
    int numClases;

    String[][] datos;
    /*Matriz con los datos parseados de cada fila*/

    Map<String, Integer> clases = new TreeMap<>();

    public Datos(int numDatos, int numAtributos, int numClases) {
        this.numDatos = numDatos;
        this.numAtributos = numAtributos;
        this.numClases = numClases;
        this.datos = new String[numDatos][numAtributos + numClases];
    }

    public Datos extraeDatosTrain(Particion idx) {
        Datos dat = new Datos(idx.getIndicesTrain().size(), this.numAtributos, this.numClases);
        for (int i = 0; i < idx.getIndicesTrain().size(); i++) {
            dat.datos[i] = this.datos[idx.getIndicesTrain().get(i)];
        }
        dat.clases = this.clases;
        return dat;
    }

    public Datos extraeDatosTest(Particion idx) {
        Datos dat = new Datos(idx.getIndicesTest().size(), this.numAtributos, this.numClases);
        for (int i = 0; i < idx.getIndicesTest().size(); i++) {
            dat.datos[i] = this.datos[idx.getIndicesTest().get(i)];
        }
        dat.clases = this.clases;
        return dat;
    }

    public Map<String, Integer> getClases() {
        return clases;
    }

    public static Datos cargaDeFichero(String nombreDeFichero) throws IOException {

        String linea;
        int i = 0, j = 0, numDatos = -1, numAtributos, numClases;

        /*Leemos el numero de lineas*/
        BufferedReader reader = new BufferedReader(new FileReader(nombreDeFichero));
        while (reader.readLine() != null) {
            numDatos++;
        }

        reader = new BufferedReader(new FileReader(nombreDeFichero));
        linea = reader.readLine();
        StringTokenizer tokens = new StringTokenizer(linea, " ");
        /*Asignamos numAtributos*/
        tokens.hasMoreTokens();
        numAtributos = Integer.parseInt(tokens.nextToken());
        /*Asignamos numClases*/
        tokens.hasMoreTokens();
        numClases = Integer.parseInt(tokens.nextToken());

        Datos datos = new Datos(numDatos, numAtributos, numClases);
        /*Rellenamos los datos*/
        while ((linea = reader.readLine()) != null) {
            tokens = new StringTokenizer(linea, " ");
            while (tokens.hasMoreTokens()) {
                datos.datos[i][j] = tokens.nextToken();
                j++;
            }
            j = 0;
            i++;
        }

        Map<String, Integer> clases = new TreeMap<>();
        Map<Integer, String> clases_reverse = new TreeMap<>();
        String aux_clases;
        int n_clases = -1;
        /*Obtenemos las distintas clases*/
        for (int k = 0; k < datos.numDatos; k++) {
            aux_clases = "";
            for (int l = 0; l < datos.numClases; l++) {
                aux_clases = aux_clases.concat(datos.getDatos()[k][datos.numAtributos + l]);
            }
            if (!clases.containsKey(aux_clases)) {
                clases.put(aux_clases, n_clases);
                clases_reverse.put(n_clases, aux_clases);
                n_clases++;
                if (n_clases == 0) {
                    n_clases++; //Queremos que sea -1 y 1
                }
            }
        }

        datos.clases = clases;
        return datos;
    }

    public int getNumDatos() {
        return numDatos;
    }

    public int getNumAtributos() {
        return numAtributos;
    }

    public int getNumClases() {
        return numClases;
    }

    public String[][] getDatos() {
        return datos;
    }

    //Metodo que recibe una linea y devuelve la clase traducida
    public Integer traducirLinea(String[] linea) {
        String aux_clases = "";
        for (int l = 0; l < this.numClases; l++)
            aux_clases = aux_clases.concat(linea[this.numAtributos + l]);
        
        return this.clases.get(aux_clases);
    }

}
