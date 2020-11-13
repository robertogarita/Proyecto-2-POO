package pruebas;

import personajes.principales.Alan;
import personajes.principales.Piece;
import ubicacion.Casillas;
import personajes.enemigos.Piedra;
import personajes.enemigos.Zombielvl1;
import personajes.objetos.Objetos;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

/*
Clase principal donde se dibuja todo
--Hay que separar lo logico de lo grafico
--Mejorar el orden de esta clase
*/

public class Grafico extends JComponent implements ActionListener{
    
    private static final long serialVersionUID = 1L;
    
    private final int Square_Width = 65, rows = 8, cols = 8;
    public static int turnCounter = 0, Selector_Azar = 3, Nivel = 1;
    
    public ArrayList<Piece> enemigos, Users;
    public ArrayList<Piedra> Obstaculo;
    public ArrayList<Objetos> ObjetosLista, Muertes;
    public ArrayList<DrawingShape> Static_Shapes, User_Graphics;
    public ArrayList<Integer> VD;
    
    public Piece Active_Piece, casillaDisparo;
    public Piedra piedraMadre;

    private JButton botonNivel, botonAtacar;
    private JLabel mensaje, mensaje2, mensaje3;
    private JProgressBar barra1=new JProgressBar(), barra2=new JProgressBar(), barra3=new JProgressBar();

    private boolean Usuario_Seleccionado = false, Ataque = false, Move = true;
    private Integer BoardGrid[][];
    private String board_file_path = "/recursos/estaticoSprites/Cuadro.png";
    private String active_square_file_path = "/recursos/estaticoSprites/active_square.png";

    /*
    initGrid: Inicializa las posiciones del tablero,
        coloca cada elemento en su array respectivo
        y coloca las barras de salud(Hay que ponerlas en funcion drawBoard)
    */
    public void initGrid(){
        if(Nivel == 1){
            for(int i=0 ; i<rows ; i++){
                for(int f=0 ; f<cols ; f++){
                    BoardGrid[i][f] = 0;
                }
            }
        }

        Users.add(new Alan(6, 0, "/recursos/userSprites/Men.png", this, 100));
        Users.add(new Alan(7, 0, "/recursos/userSprites/Bigote.png", this, 100));
        Users.add(new Alan(7, 1, "/recursos/userSprites/Johnny.png", this, 100));

        Obstaculo.add(new Piedra(3, 5, "/recursos/estaticoSprites/piedra.png", this));

        if(Nivel == 1){
            enemigos.add(new Zombielvl1(2, 6, "/recursos/enemigosSprites/ZombieLVL1.png", this, 100));
            enemigos.add(new Zombielvl1(5, 5, "/recursos/enemigosSprites/ZombieLVL1.png", this, 100));
            enemigos.add(new Zombielvl1(3, 3, "/recursos/enemigosSprites/ZombieLVL1.png", this, 100));
        }else if(Nivel == 2){
            enemigos.add(new Zombielvl1(2, 6, "/recursos/enemigosSprites/ZombieLVL2.png", this, 100));
            enemigos.add(new Zombielvl1(5, 5, "/recursos/enemigosSprites/ZombieLVL1.png", this, 100));
            enemigos.add(new Zombielvl1(3, 3, "/recursos/enemigosSprites/ZombieLVL2.png", this, 100));
        }else if(Nivel == 3){
            enemigos.add(new Zombielvl1(2, 6, "/recursos/enemigosSprites/ZombieLVL2.png", this, 100));
            enemigos.add(new Zombielvl1(5, 5, "/recursos/enemigosSprites/ZombieLVL2.png", this, 100));
            enemigos.add(new Zombielvl1(3, 3, "/recursos/enemigosSprites/ZombieLVL3.png", this, 100));
        }else if(Nivel == 4){
            System.exit(0);
        }
        if(Nivel > 1){
            botonNivel.setEnabled(false);
            drawBoard();
        }
        barra1.setBounds(525, 100, 150, 10);
        barra1.setValue(70);
        barra1.setBackground(Color.RED);
        barra1.setForeground(Color.GREEN);
        add(barra1);

        barra2.setBounds(525, 140, 150, 10);
        barra2.setValue(50);
        barra2.setBackground(Color.RED);
        barra2.setForeground(Color.GREEN);
        add(barra2);

        barra3.setBounds(525, 180, 150, 10);
        barra3.setValue(92);
        barra3.setBackground(Color.RED);
        barra3.setForeground(Color.GREEN);
        add(barra3);
    }

    /*
    Grafico: Coloca los botones
        inicializa las variables(Arrays principalmente)
        coloca el fondo y el tamanho de la ventana
    */
    public Grafico(){
        botonNivel = new JButton("Siguiente");
        botonNivel.setBounds(526, 460, 150, 50);
        botonNivel.addActionListener(this);
        botonNivel.setEnabled(false);
        add(botonNivel);

        botonAtacar = new JButton("Atacar");
        botonAtacar.setBounds(596, 20, 85, 30);
        botonAtacar.addActionListener(this);
        add(botonAtacar);

        mensaje = new JLabel("Zombie 1");
        mensaje.setBounds(526, 65, 150, 50);
        add(mensaje);
        mensaje2 = new JLabel("Zombie 2");
        mensaje2.setBounds(526, 105, 150, 50);
        add(mensaje2);
        mensaje3 = new JLabel("Zombie 3");
        mensaje3.setBounds(526, 145, 150, 50);
        add(mensaje3);

        VD = new ArrayList<Integer>();
        BoardGrid = new Integer[rows][cols];
        Static_Shapes = new ArrayList<DrawingShape>();
        User_Graphics = new ArrayList<DrawingShape>();
        Users = new ArrayList<Piece>();
        enemigos = new ArrayList<Piece>();
        Obstaculo = new ArrayList<Piedra>();
        ObjetosLista = new ArrayList<Objetos>();
        Muertes = new ArrayList<Objetos>();

        initGrid();

        this.setBackground(new Color(37,13,84));
        this.setPreferredSize(new Dimension(520, 520));
        this.setMinimumSize(new Dimension(100, 100));
        this.setMaximumSize(new Dimension(1000, 1000));

        this.addMouseListener(mouseAdapter);

        this.setVisible(true);
        this.requestFocus();
        drawBoard();
    }

    /*
    drawBoard: Prepara los arrays, los cuales son los que
        se van a utilizar para dibujar todos elementos
    */
    public void drawBoard(){
        User_Graphics.clear();
        Static_Shapes.clear();

        if (enemigos.isEmpty()){
            if(Nivel == 3){
                mensaje = new JLabel("Ya ganaste");
                mensaje.setBounds(526, 300, 150, 50);
                add(mensaje);
            }
            botonNivel.setEnabled(true);
        }
        //Cargar la imagen del tablero
        Image board = loadImage(board_file_path);
        Static_Shapes.add(new DrawingImage(board, new Rectangle2D.Double(0,0, board.getWidth(null), board.getHeight(null))));
        
        //Cargar la imagen de los obstaculos
        Image Piedra1 = loadImage("/recursos/estaticoSprites/piedra.png");
        Static_Shapes.add(new DrawingImage(Piedra1, new Rectangle2D.Double(Square_Width*5,
            Square_Width*3, Piedra1.getWidth(null), Piedra1.getHeight(null))));

        //Funcion moverZombie
        if(turnCounter%2 != 0 && enemigos.isEmpty() == false){
            int num = (int)(Math.random() * Selector_Azar);
            MoverZombie(num);
        }
        /*
        Dibuja un cuadro rojo en debajo de cada personaje
        seleccionado
        */
        if(Active_Piece != null){
            if(!enemigos.contains(Active_Piece)){
                //active_Square: Cuadro rojo
                Image active_Square = loadImage(active_square_file_path);
                //casillaDisparo: Cuando se selecciona atacar, dibuja cuadros de ataque
                Image casillaDisparo = loadImage("/recursos/estaticoSprites/disparo.png");

                Static_Shapes.add(new DrawingImage(active_Square, new Rectangle2D.Double(Square_Width*Active_Piece.x,
                    Square_Width*Active_Piece.y, active_Square.getWidth(null), active_Square.getHeight(null))));

                if(Ataque){
                    //Llama constructos clase Casilla
                    Casillas valor = new Casillas(Active_Piece.x, Active_Piece.y);
                    VD = valor.Valores;
                    Ataque = false;

                    //Carga las imagenes de cuadro de ataque
                    for(int i=0 ; i<VD.size() ; i+=2){
                        Static_Shapes.add(new DrawingImage(casillaDisparo, new Rectangle2D.Double(Square_Width*VD.get(i),
                            Square_Width*VD.get(i+1), active_Square.getWidth(null), active_Square.getHeight(null))));
                    }
                }
            }
        }else{
            botonAtacar.setEnabled(false);
        }
        for(int i=0 ; i<Users.size() ; i++){
            int COL = Users.get(i).x;
            int ROW = Users.get(i).y;

            //Carga las imagenes de cada personaje
            Image piece = loadImage(Users.get(i).file_path);
            User_Graphics.add(new DrawingImage(piece, new Rectangle2D.Double(Square_Width*COL, 
                Square_Width*ROW, piece.getWidth(null), piece.getHeight(null))));
        }

        for(int i=0 ; i<enemigos.size() ; i++){
            int COL = enemigos.get(i).x;
            int ROW = enemigos.get(i).y;

            //Carga las imagenes de cada enemigos
            Image piece = loadImage(enemigos.get(i).file_path);
            User_Graphics.add(new DrawingImage(piece, new Rectangle2D.Double(Square_Width*COL, 
                Square_Width*ROW, piece.getWidth(null), piece.getHeight(null))));
        }
        if(!ObjetosLista.isEmpty()){
            //Carga las imagenes de los obj. dropeados
            Image Objeto = loadImage("/recursos/estaticoSprites/Objeto1.png");
            for(int i=0 ; i<ObjetosLista.size() ; i++){
                Static_Shapes.add(new DrawingImage(Objeto, new Rectangle2D.Double(Square_Width*ObjetosLista.get(i).x,
                Square_Width*ObjetosLista.get(i).y, Objeto.getWidth(null), Objeto.getHeight(null))));
            }
        }
        if(!Muertes.isEmpty()){
            //Carga las imagenes de las lapidas
            Image lapida = loadImage("/recursos/estaticoSprites/Lapida.png");
            for(int i=0 ; i<Muertes.size() ; i++){
                Static_Shapes.add(new DrawingImage(lapida, new Rectangle2D.Double(Square_Width*Muertes.get(i).x,
                Square_Width*Muertes.get(i).y, lapida.getWidth(null), lapida.getHeight(null))));
            }
        }
        //Llama paintComponent
        repaint();
    }

    /* 
    Mueve cada enemigo del mapa, utilizando la clase Piece
        tambien, si hay un personaje cerca, lo mata/danha
    */
    private void MoverZombie(int num){
        turnCounter ++;
        for(int i=0 ; i<enemigos.size() ; i++){
            Active_Piece = enemigos.get(i);
            if(Active_Piece.piezaAdyacentes(Active_Piece.x , Active_Piece.y)){
                Active_Piece.MoveEnemy();
            }else{
                Users.remove(getPiece(Piece.x_User, Piece.y_User));
                Muertes.add(new Objetos(Piece.x_User, Piece.y_User, "/recursos/estaticoSprites/Lapida.png"));
            }
        }
        drawBoard();
        Active_Piece = null;
    }

    //Metodo CargarImagen, lee las imagenes desde recursos
    private Image loadImage(String file_path){
        try{
            return ImageIO.read(getClass().getResourceAsStream(file_path));
        }catch(IOException E){
            return new BufferedImage(10,10, BufferedImage.TYPE_INT_ARGB);
        }
    }

    //Metodo para obtener un personaje o enemigo, devuelve tipo Piece
    public Piece getPiece(int x, int y){
        for(Piece p : Users){
            if(p.x == x && p.y == y){
                return p;
            }
        }
        for(Piece p : enemigos){
            if(p.x == x && p.y == y){
                return p;
            }
        }
        return null;
    }

    //Metodo para obtener un obstaculo, devuelve tipo Piedra
    public Piedra getObstaculo(int x, int y){
        for(Piedra a : Obstaculo){
            if(a.x == x && a.y == y){
                return a;
            }
        }
        return null;
    }

    @Override
    //Activar funcionamiento a los botones
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonNivel){
            Static_Shapes.clear();
            User_Graphics.clear();
            Users.clear();
            enemigos.clear();
            Obstaculo.clear();
            Selector_Azar = 3;
            Nivel++;
            initGrid();
        }
        if (e.getSource() == botonAtacar){
            botonAtacar.setEnabled(false);
            Ataque = true;
            Move = false;
            drawBoard();
        }
    }

    //Activar las funciones del mouse
    private MouseAdapter mouseAdapter = new MouseAdapter(){
        
        public void mouseClicked(MouseEvent e){
            //Obtiene la posicion de la fila y columna clickeada
            int d_X = e.getX();
            int d_Y = e.getY();
            int Clicked_Row = d_Y / Square_Width;
            int Clicked_Column = d_X / Square_Width;

            Piece clicked_piece = getPiece(Clicked_Column, Clicked_Row);
            Piedra clicked_obstacule = getObstaculo(Clicked_Row, Clicked_Column);
            //Obtiene el personaje/enemigo/obtaculo con el metodo getPiece/getObstacule

            if(false == enemigos.contains(clicked_piece) && clicked_piece != null){
                //Permite seleccionar el personaje y guardarlo en Active_Piece
                Active_Piece = clicked_piece;
                Usuario_Seleccionado = true;
                botonAtacar.setEnabled(true);
            }else if(Usuario_Seleccionado == true && Clicked_Column < 8){
                if((clicked_obstacule == null) && Move){
                    if(Active_Piece.canMove(Clicked_Row, Clicked_Column)&& Users.contains(clicked_piece) == false){
                        
                        //Mueve el personaje a la posicion seleccionada
                        Active_Piece.x = Clicked_Column;
                        Active_Piece.y = Clicked_Row;

                        turnCounter ++;

                        Active_Piece = null;
                        Usuario_Seleccionado = false;
                    }
                }else{
                    //Permite seleccionar casillas para atacar
                    Move = true;
                    if(Active_Piece.Atacar(Active_Piece.x, Active_Piece.y, Clicked_Column, Clicked_Row)){
                        if (enemigos.contains(clicked_piece)){
                            clicked_piece.recibirDano();
                            if(clicked_piece.Salud == 0){
                                enemigos.remove(clicked_piece);
                                Selector_Azar--;
                                ObjetosLista.add(new Objetos(Clicked_Column, Clicked_Row, 
                                    "/recursos/estaticoSprites/Objeto1.png"));
                            }
                            turnCounter ++;
                        }
                    }
                    Active_Piece = null;
                    Usuario_Seleccionado = false;
                }
            }
            drawBoard();
        }
    };

    /*
    Todos los elementos se dibujan apartir de aqui
    */
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        drawBackground(g2);
        drawShapes(g2);
        drawInventory(g2);
    }

    private void drawBackground(Graphics2D g2){
        g2.setColor(Color.CYAN);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
    private void drawShapes(Graphics2D g2){
        for(DrawingShape shape : Static_Shapes){
            shape.draw(g2);
        }
        for(DrawingShape shape : User_Graphics){
            shape.draw(g2);
        }
    }
    private void drawInventory(Graphics g2){
        g2.setColor(Color.GREEN);
        g2.fillRect(530, 265, 145, 180);
    }
}

interface DrawingShape{
    boolean contains(Graphics2D g2, double x, double y);
    void adjustPosition(double dx, double dy);
    void draw(Graphics2D g2); 
}


class DrawingImage implements DrawingShape{
    
    public Image imagen;
    public Rectangle2D rect;

    public DrawingImage(Image imagen, Rectangle2D rect){
        this.imagen = imagen;
        this.rect = rect;
    }

    public boolean contains(Graphics2D g2, double x, double y){
        return rect.contains(x, y);
    }
    public void adjustPosition(double dx, double dy){
        rect.setRect(rect.getX() + dx, rect.getY() + dy,
            rect.getWidth(), rect.getHeight());
    }
    public void draw(Graphics2D g2){
        Rectangle2D bounds = rect.getBounds2D();
        g2.drawImage(imagen, (int)bounds.getMinX(), (int)bounds.getMinY(),
            (int)bounds.getMaxX(), (int)bounds.getMaxY(), 0, 0, imagen.getWidth(null), imagen.getHeight(null), null);
    }
}

