package personajes.principales;

import pruebas.Grafico;

public class Piece {
    
    public int x;
    public int y;
    public String file_path;
    public Grafico board;

    public Piece(int x, int y, String file_path, Grafico board){
        this.x = x;
        this.y = y;
        this.file_path = file_path;
        this.board = board;
    }

    public boolean canMove(int d_Y, int d_X){
        if((x == d_X ||x == d_X-1 || x == d_X+1) && (y == d_Y || y == d_Y-1 || y == d_Y+1)){
            return true;
        }else{
            return false;
        }
    }
}
