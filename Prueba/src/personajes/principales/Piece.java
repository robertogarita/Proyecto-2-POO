package personajes.principales;

import java.util.Random;

import javax.swing.JProgressBar;

import pruebas.Grafico;

/* 
Clase que maneja todos los metodos de enemigos y personajes
*/

public class Piece {
    
    public int x;
    public int y;
    public String file_path;
    public Grafico board;
    public int Salud;
    public static int Danho = 25;
    public static int x_User;
    public static int y_User;
    public JProgressBar SaludBarra;

    public Piece(int x, int y, String file_path, Grafico board, int Salud, JProgressBar SaludBarra){
        this.x = x;
        this.y = y;
        this.file_path = file_path;
        this.board = board;
        this.Salud = Salud;
        this.SaludBarra = SaludBarra;
    }

    //Verifica si el personaje se puede mover a la casilla seleccionada
    public boolean canMove(int d_Y, int d_X){
        if((x == d_X ||x == d_X-1 || x == d_X+1) && (y == d_Y || y == d_Y-1 || y == d_Y+1)){
            return true;
        }else{
            return false;
        }
    }

    //Busca si el enemigo tiene algun usuario cerca, si lo tiene, permite ubicarlo para atacarlo
    public boolean piezaAdyacentes(int Dx, int Dy){
        Piece pieza1 = board.getPiece(Dx+1, Dy);
        Piece pieza2 = board.getPiece(Dx-1, Dy);
        Piece pieza3 = board.getPiece(Dx, Dy+1);
        Piece pieza4 = board.getPiece(Dx, Dy-1);
        if(!(board.Users.contains(pieza1) || board.Users.contains(pieza2) ||
            board.Users.contains(pieza3) || board.Users.contains(pieza4))){
            return true;
        }else{
            if(pieza1 != null){
                x_User = pieza1.x;
                y_User = pieza1.y;
            }
            else if(pieza2 != null){
                x_User = pieza2.x;
                y_User = pieza2.y;
            }
            else if(pieza3 != null){
                x_User = pieza3.x;
                y_User = pieza3.y;
            }
            else if(pieza4 != null){
                x_User = pieza4.x;
                y_User = pieza4.y;
            }
            return false;
        }
    }

    //Mueve el enemigo una casilla
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

    public void AumentarDanho(){
        Danho += 25;
    }

    //Baja la salud del enemigo cuando lo atacan
    public void recibirDano(){
        Salud -= Danho;
    }

    //Permite a los usuarios atacar en las casillas marcadas
    public boolean Atacar(int xPosicion, int yPosicion, int xLlegada, int yLlegada){
        if((xPosicion-2 <= xLlegada && xLlegada <= xPosicion+2) && 
            (yPosicion-2 <= yLlegada && yLlegada <= yPosicion+2)){
                return true;
            }else{
                return false;
            }
    }
}
