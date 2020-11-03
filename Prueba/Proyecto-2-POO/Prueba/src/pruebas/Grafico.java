package pruebas;

import personajes.principales.Alan;
import personajes.principales.Piece;
import personajes.enemigos.Piedra;
import personajes.enemigos.Zombielvl1;

import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

public class Grafico extends JComponent{
    
    private final int Square_Width = 65;
    public static int turnCounter = 0;
    
    public ArrayList<Piece> enemigos;
    public ArrayList<Piedra> Obstaculo;
    public ArrayList<Piece> Users;
    public ArrayList<DrawingShape> Static_Shapes;
    public ArrayList<DrawingShape> User_Graphics;

    public Piece Active_Piece;
    public Piedra piedraMadre;

    private boolean Usuario_Seleccionado = false;
    private final int rows = 8;
    private final int cols = 8;
    private Integer BoardGrid[][];
    private String board_file_path = "/recursos/Cuadro.png";
    private String active_square_file_path = "/recursos/active_square.png";

    public void initGrid(){
        for(int i=0 ; i<rows ; i++){
            for(int f=0 ; f<cols ; f++){
                BoardGrid[i][f] = 0;
            }
        }
        Users.add(new Alan(6, 0, "/recursos/Men.png", this));
        Obstaculo.add(new Piedra(3, 5, "/recursos/piedra.png", this));
        enemigos.add(new Zombielvl1(2, 6, "/recursos/ZombieLVL1.png", this));
    }

    public Grafico(){
        BoardGrid = new Integer[rows][cols];
        Static_Shapes = new ArrayList<DrawingShape>();
        User_Graphics = new ArrayList<DrawingShape>();
        Users = new ArrayList<Piece>();
        enemigos = new ArrayList<Piece>();
        Obstaculo = new ArrayList<Piedra>();

        initGrid();

        this.setBackground(new Color(37,13,84));
        this.setPreferredSize(new Dimension(520, 520));
        this.setMinimumSize(new Dimension(100, 100));
        this.setMaximumSize(new Dimension(1000, 1000));

        this.addMouseListener(mouseAdapter);
        //this.addComponentListener(componentAdapter);
        //this.addKeyListener(keyAdapter);

        this.setVisible(true);
        this.requestFocus();
        drawBoard();
    }

    public void drawBoard(){
        User_Graphics.clear();
        Static_Shapes.clear();

        Image board = loadImage(board_file_path);
        Static_Shapes.add(new DrawingImage(board, new Rectangle2D.Double(0,0, board.getWidth(null), board.getHeight(null))));
        
        Image Piedra1 = loadImage("/recursos/piedra.png");
        Static_Shapes.add(new DrawingImage(Piedra1, new Rectangle2D.Double(Square_Width*5,
        Square_Width*3, Piedra1.getWidth(null), Piedra1.getHeight(null))));

        if(Active_Piece != null && Active_Piece != enemigos.get(0)){
            Image active_Square = loadImage(active_square_file_path);
            Static_Shapes.add(new DrawingImage(active_Square, new Rectangle2D.Double(Square_Width*Active_Piece.x,
                Square_Width*Active_Piece.y, active_Square.getWidth(null), active_Square.getHeight(null))));
        }
        for(int i=0 ; i<Users.size() ; i++){
            int COL = Users.get(i).x;
            int ROW = Users.get(i).y;
            Image piece = loadImage("/recursos/Men.png");
            User_Graphics.add(new DrawingImage(piece, new Rectangle2D.Double(Square_Width*COL, 
                Square_Width*ROW, piece.getWidth(null), piece.getHeight(null))));
        }

        for(int i=0 ; i<enemigos.size() ; i++){
            int COL = enemigos.get(i).x;
            int ROW = enemigos.get(i).y;
            Image piece = loadImage("/recursos/ZombieLVL1.png");
            User_Graphics.add(new DrawingImage(piece, new Rectangle2D.Double(Square_Width*COL, 
                Square_Width*ROW, piece.getWidth(null), piece.getHeight(null))));
        }
        if(turnCounter%2 != 0){
            VerificarTurno();
        }
        repaint();
    }

    private void VerificarTurno(){
        turnCounter ++;
        Active_Piece = enemigos.get(0);
        int num1 = (int)(Math.random() * 2);
        int dir = (int)(Math.random() * 2);
        switch(num1){
            case 0:
                if(1 <= Active_Piece.x && Active_Piece.x <= 6){
                    switch(dir){
                        case 0:
                        Active_Piece.x ++;
                        case 1:
                        Active_Piece.x --;
                    }
                }else{
                    if(Active_Piece.x <= 0){
                        Active_Piece.x ++;
                    }else{
                        Active_Piece.x --;
                    }
                }
            case 1:
                if(1 <= Active_Piece.y && Active_Piece.y <= 6){
                    switch(dir){
                        case 0:
                        Active_Piece.y ++;
                        case 1:
                        Active_Piece.y --;
                    }
                }else{
                    if(Active_Piece.y <= 0){
                        Active_Piece.y ++;
                    }else{
                        Active_Piece.y --;
                    }
                }
            }
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

    
    private MouseAdapter mouseAdapter = new MouseAdapter(){
        public void mouseClicked(MouseEvent e){
            int d_X = e.getX();
            int d_Y = e.getY();
            int Clicked_Row = d_Y / Square_Width;
            int Clicked_Column = d_X / Square_Width;

            Piece clicked_piece = getPiece(Clicked_Column, Clicked_Row);
            Piedra clicked_obstacule = getObstaculo(Clicked_Row, Clicked_Column);

            if(clicked_piece != null && clicked_piece != enemigos.get(0)){
                Active_Piece = clicked_piece;
                Usuario_Seleccionado = true;
            }else if(Usuario_Seleccionado == true && clicked_piece == null){
                if(clicked_obstacule == null){
                    if(Active_Piece.canMove(Clicked_Row, Clicked_Column) == true){

                        System.out.println(clicked_piece);

                        Active_Piece.x = Clicked_Column;
                        Active_Piece.y = Clicked_Row;

                        turnCounter ++;

                        Active_Piece = null;
                        Usuario_Seleccionado = false;
                    }
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

