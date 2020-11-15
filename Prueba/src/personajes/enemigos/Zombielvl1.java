package personajes.enemigos;

import javax.swing.JProgressBar;

import personajes.principales.Piece;
import pruebas.Grafico;

/* 
Se comunica con la clase Piece
*/

public class Zombielvl1 extends Piece {
    
    public Zombielvl1(int x, int y, String file_path, Grafico board, int Salud, JProgressBar SaludBarra) {
        super(x, y, file_path, board, Salud, SaludBarra);
    }
}