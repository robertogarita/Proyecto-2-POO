package personajes.objetos;

import pruebas.Grafico;

public class Objetos{
    public int x;
    public int y;
    public String file_path;
    public Grafico board;

    public Objetos(int x, int y, String file_path){
        this.x = x;
        this.y = y;
        this.file_path = file_path;
        //this.board = board;
    }
}
