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

    public void FillList(){
        board.Obstacule.add(new Piedra(1, 1, "/recursos/estaticoSprites/Obstaculo.png", board));
        board.Obstacule.add(new Piedra(2, 1, "/recursos/estaticoSprites/Obstaculo.png", board));
        board.Obstacule.add(new Piedra(4, 1, "/recursos/estaticoSprites/Obstaculo.png", board));
        board.Obstacule.add(new Piedra(1, 3, "/recursos/estaticoSprites/Obstaculo.png", board));
        board.Obstacule.add(new Piedra(5, 3, "/recursos/estaticoSprites/Obstaculo.png", board));
        board.Obstacule.add(new Piedra(7, 3, "/recursos/estaticoSprites/Obstaculo.png", board));
        board.Obstacule.add(new Piedra(1, 5, "/recursos/estaticoSprites/Obstaculo.png", board));
        board.Obstacule.add(new Piedra(2, 5, "/recursos/estaticoSprites/Obstaculo.png", board));
        board.Obstacule.add(new Piedra(1, 6, "/recursos/estaticoSprites/Obstaculo.png", board));
        board.Obstacule.add(new Piedra(7, 6, "/recursos/estaticoSprites/Obstaculo.png", board));
    }
}
