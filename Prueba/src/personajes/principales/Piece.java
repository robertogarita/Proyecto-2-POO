package personajes.principales;

import java.util.Random;

import pruebas.Grafico;

public class Piece {
    
    public int x;
    public int y;
    public String file_path;
    public Grafico board;
    public int Salud;

    public Piece(int x, int y, String file_path, Grafico board, int Salud){
        this.x = x;
        this.y = y;
        this.file_path = file_path;
        this.board = board;
        this.Salud = Salud;
    }

    public boolean canMove(int d_Y, int d_X){
        if((x == d_X ||x == d_X-1 || x == d_X+1) && (y == d_Y || y == d_Y-1 || y == d_Y+1)){
            return true;
        }else{
            return false;
        }
    }

    public boolean piezaAdyacentes(int Dx, int Dy){
        if(board.getPiece(Dx+1, Dy) == null && board.getPiece(Dx-1, Dy) == null &&
            board.getPiece(Dx, Dy+1) == null && board.getPiece(Dx, Dy-1) == null){
                return true;
            }else{
                return false;
            }
    }

    public void MoveEnemy(){
        Random aleatorio = new Random();
        int intAle = aleatorio.nextInt(4);
        switch(intAle){
            case 0:
                x++;
                if(x > 7){
                    x--;
                }
                break;

            case 1:
                x--;
                if(x < 0){
                    x++;
                }
                break;

            case 2:
                y++;
                if(y > 7){
                    y--;
                }
                break;

            case 3:
                y--;
                if(y < 0){
                    y++;
                }
                break;
        }
    }

    public boolean canAtack(){
        return true;
    }

    public void dropObject(){}

    public void recibirDano(){
        Salud -= 25;
    }

    public boolean Atacar(int xPosicion, int yPosicion, int xLlegada, int yLlegada){
        if((xPosicion-2 <= xLlegada && xLlegada <= xPosicion+2) && 
            (yPosicion-2 <= yLlegada && yLlegada <= yPosicion+2)){
                return true;
            }else{
                return false;
            }
    }
}
