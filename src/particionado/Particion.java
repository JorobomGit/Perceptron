package particionado;

import java.util.ArrayList;

public class Particion {
    
    private ArrayList<Integer> indicesTrain;
    private ArrayList<Integer> indicesTest;    

    public Particion(ArrayList<Integer> indTrain, ArrayList<Integer> indTest) {
        this.indicesTrain = indTrain;
        this.indicesTest = indTest;
    }

    public ArrayList<Integer> getIndicesTrain() {
        return indicesTrain;
    }

    public ArrayList<Integer> getIndicesTest() {
        return indicesTest;
    }
    
}
