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

public class Grafico extends JComponent implements ActionListener{
    
    private static final long serialVersionUID = 1L;
    
    private final int Square_Width = 65, rows = 8, cols = 8;
    public static int turnCounter = 0, Selector_Azar = 3, Nivel = 1;
    
    public ArrayList<Piece> enemigos, Users;
    public ArrayList<Piedra> Obstaculo;
    public ArrayList<Objetos> ObjetosLista;
    public ArrayList<DrawingShape> Static_Shapes, User_Graphics;
    public ArrayList<Integer> VD;
    
    public Piece Active_Piece, casillaDisparo;
    public Piedra piedraMadre;

    private JButton botonNivel, botonAtacar;
    private JLabel mensaje, mensaje2, mensaje3;

    private boolean Usuario_Seleccionado = false, Ataque = false, Move = true;
    private Integer BoardGrid[][];
    private String board_file_path = "/recursos/estaticoSprites/Cuadro.png";
    private String active_square_file_path = "/recursos/estaticoSprites/active_square.png";

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
    }

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

        mensaje = new JLabel("Zombie 1:");
        mensaje.setBounds(526, 100, 150, 50);
        add(mensaje);
        mensaje2 = new JLabel("Zombie 2:");
        mensaje2.setBounds(526, 125, 150, 50);
        add(mensaje2);
        mensaje3 = new JLabel("Zombie 3:");
        mensaje3.setBounds(526, 150, 150, 50);
        add(mensaje3);

        VD = new ArrayList<Integer>();
        BoardGrid = new Integer[rows][cols];
        Static_Shapes = new ArrayList<DrawingShape>();
        User_Graphics = new ArrayList<DrawingShape>();
        Users = new ArrayList<Piece>();
        enemigos = new ArrayList<Piece>();
        Obstaculo = new ArrayList<Piedra>();
        ObjetosLista = new ArrayList<Objetos>();

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

    public void drawBoard(){
        User_Graphics.clear();
        Static_Shapes.clear();

        mensaje.setText("Zombie 1: " + enemigos.get(0).Salud);
        mensaje2.setText("Zombie 2: " + enemigos.get(1).Salud);
        mensaje3.setText("Zombie 3: " + enemigos.get(2).Salud);

        if (enemigos.isEmpty()){
            if(Nivel == 3){
                mensaje = new JLabel("Ya ganaste");
                mensaje.setBounds(526, 300, 150, 50);
                add(mensaje);
            }
            botonNivel.setEnabled(true);
        }

        Image board = loadImage(board_file_path);
        Static_Shapes.add(new DrawingImage(board, new Rectangle2D.Double(0,0, board.getWidth(null), board.getHeight(null))));
        
        Image Piedra1 = loadImage("/recursos/estaticoSprites/piedra.png");
        Static_Shapes.add(new DrawingImage(Piedra1, new Rectangle2D.Double(Square_Width*5,
            Square_Width*3, Piedra1.getWidth(null), Piedra1.getHeight(null))));

        if(turnCounter%2 != 0 && enemigos.isEmpty() == false){
            int num = (int)(Math.random() * Selector_Azar);
            MoverZombie(num);
        }
        if(Active_Piece != null){
            if(!enemigos.contains(Active_Piece)){
                Image active_Square = loadImage(active_square_file_path);
                Image casillaDisparo = loadImage("/recursos/estaticoSprites/disparo.png");

                Static_Shapes.add(new DrawingImage(active_Square, new Rectangle2D.Double(Square_Width*Active_Piece.x,
                    Square_Width*Active_Piece.y, active_Square.getWidth(null), active_Square.getHeight(null))));

                if(Ataque){
                    Casillas valor = new Casillas(Active_Piece.x, Active_Piece.y);
                    VD = valor.Valores;
                    Ataque = false;
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
            Image piece = loadImage(Users.get(i).file_path);
            User_Graphics.add(new DrawingImage(piece, new Rectangle2D.Double(Square_Width*COL, 
                Square_Width*ROW, piece.getWidth(null), piece.getHeight(null))));
        }

        for(int i=0 ; i<enemigos.size() ; i++){
            int COL = enemigos.get(i).x;
            int ROW = enemigos.get(i).y;
            Image piece = loadImage(enemigos.get(i).file_path);
            User_Graphics.add(new DrawingImage(piece, new Rectangle2D.Double(Square_Width*COL, 
                Square_Width*ROW, piece.getWidth(null), piece.getHeight(null))));
        }
        if(!ObjetosLista.isEmpty()){
            Image Objeto = loadImage("/recursos/estaticoSprites/Objeto1.png");
            Static_Shapes.add(new DrawingImage(Objeto, new Rectangle2D.Double(Square_Width*ObjetosLista.get(0).x,
                Square_Width*ObjetosLista.get(0).y, Objeto.getWidth(null), Objeto.getHeight(null))));
        }
        repaint();
    }

    private void MoverZombie(int num){
        turnCounter ++;
        for(int i=0 ; i<enemigos.size() ; i++){
            Active_Piece = enemigos.get(i);
            if(Active_Piece.piezaAdyacentes(Active_Piece.x , Active_Piece.y)){
                Active_Piece.MoveEnemy();
            }
        }
        drawBoard();
        Active_Piece = null;
    }

    private Image loadImage(String file_path){
        try{
            return ImageIO.read(getClass().getResourceAsStream(file_path));
        }catch(IOException E){
            return new BufferedImage(10,10, BufferedImage.TYPE_INT_ARGB);
        }
    }

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
    public Piedra getObstaculo(int x, int y){
        for(Piedra a : Obstaculo){
            if(a.x == x && a.y == y){
                return a;
            }
        }
        return null;
    }

    @Override
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

    private MouseAdapter mouseAdapter = new MouseAdapter(){
        
        public void mouseClicked(MouseEvent e){
            int d_X = e.getX();
            int d_Y = e.getY();
            int Clicked_Row = d_Y / Square_Width;
            int Clicked_Column = d_X / Square_Width;

            Piece clicked_piece = getPiece(Clicked_Column, Clicked_Row);
            Piedra clicked_obstacule = getObstaculo(Clicked_Row, Clicked_Column);

            if(false == enemigos.contains(clicked_piece) && clicked_piece != null){
                Active_Piece = clicked_piece;
                Usuario_Seleccionado = true;
                botonAtacar.setEnabled(true);
            }else if(Usuario_Seleccionado == true && Clicked_Column < 8){
                if((clicked_obstacule == null) && Move){
                    if(Active_Piece.canMove(Clicked_Row, Clicked_Column)&& Users.contains(clicked_piece) == false){

                        Active_Piece.x = Clicked_Column;
                        Active_Piece.y = Clicked_Row;

                        turnCounter ++;

                        Active_Piece = null;
                        Usuario_Seleccionado = false;
                    }
                }else{
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

    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        drawBackground(g2);
        drawShapes(g2);
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

