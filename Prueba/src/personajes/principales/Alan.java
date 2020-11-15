package personajes.principales;

import javax.swing.JProgressBar;

import pruebas.Grafico;

/* 
Se comunica con la clase Piece
*/

public class Alan extends Piece {

    public Alan(int x, int y, String file_path, Grafico board, int Salud, JProgressBar SaludBarra) {
        super(x, y, file_path, board, Salud, SaludBarra);
    }
}
