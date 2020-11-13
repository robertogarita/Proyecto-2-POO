package ubicacion;

import java.util.ArrayList;

import pruebas.Grafico;

/*
Clase que pasa las posiciones las cuales
los cuadros de ataque se van a ubicar
*/

public class Casillas extends Grafico{

    public ArrayList<Integer> Valores = new ArrayList<Integer>();

    public Casillas(int x, int y){
        /*
        Ejemplo de funcionamiento:
        [ ][ ][X][ ][ ]
        [ ][ ][X][ ][ ]
        [X][X][O][X][X]
        [ ][ ][X][ ][ ]
        [ ][ ][X][ ][ ]
        
        O = Personaje ; Posicion(2,2)
        X = Ataque

        Va a regresar las posiciones de las X, en un arreglo
        */
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
