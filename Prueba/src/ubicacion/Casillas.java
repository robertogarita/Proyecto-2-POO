package ubicacion;

import java.util.ArrayList;

import pruebas.Grafico;

public class Casillas extends Grafico{

    public ArrayList<Integer> Valores = new ArrayList<Integer>();

    public Casillas(int x, int y){

        for(int i=1 ; i<3 ; i++){;
            if(x+i < 8){
                Valores.add(x+i);
                Valores.add(y);
            }
            if(x-i > -1){
                Valores.add(x-i);
                Valores.add(y);
            }
            if(y+i < 8){
                Valores.add(x);
                Valores.add(y+i);
            }
            if(y-i > -1){
                Valores.add(x);
                Valores.add(y-i);
            }
        }
    }    
}
