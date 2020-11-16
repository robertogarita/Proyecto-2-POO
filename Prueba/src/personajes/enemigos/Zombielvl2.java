package personajes.enemigos;

import javax.swing.JProgressBar;
import personajes.principales.Piece;
import pruebas.Grafico;

public class Zombielvl2 extends Piece{
    
    public Zombielvl2(int x, int y, String file_path, Grafico board, int Salud, JProgressBar SaludBarra) {
        super(x, y, file_path, board, Salud, SaludBarra);
    }
}
