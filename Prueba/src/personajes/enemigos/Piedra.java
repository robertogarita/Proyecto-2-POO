package personajes.enemigos;

import pruebas.Grafico;

public class Piedra {
    
    public int x;
    public int y;
    public String file_path;
    public Grafico board;

    public Piedra(int x, int y, String file_path, Grafico board){
        this.x = x;
        this.y = y;
        this.file_path = file_path;
        this.board = board;
    }
}
