package pruebas;

import personajes.principales.Character;
import personajes.principales.Piece;
import ubicacion.Casillas;
import personajes.enemigos.*;
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
    private static int turnCounter = 0, turnSelector = 3, level = 1;
    
    public ArrayList<Piece> enemies, Users;
    public ArrayList<Piedra> Obstacule;
    public ArrayList<Objetos> ObjectList, DeadUser, Inventory;
    public ArrayList<DrawingShape> Static_Shapes, User_Graphics;
    public ArrayList<Integer> AttackValues;
    
    public Piece Active_Piece;

    private JButton LevelButton, AttackButton;
    private JLabel Message1, Message2, Message3;
    private JProgressBar HPEnemy_1=new JProgressBar(), HPEnemy_2=new JProgressBar(), HPEnemy_3=new JProgressBar();

    private boolean Selected_User = false, Attack = false, Move = true;
    private Integer BoardGrid[][];
    private String board_file_path = "/recursos/estaticoSprites/Cuadro.png";
    private String active_square_file_path = "/recursos/estaticoSprites/active_square.png";

    /*
    initGrid: Inicializa las posiciones del tablero,
        coloca cada elemento en su array respectivo
        y coloca las barras de salud(Hay que ponerlas en funcion drawBoard)
    */
    public void initGrid(){
        if(level == 1){
            for(int i=0 ; i<rows ; i++){
                for(int f=0 ; f<cols ; f++){
                    BoardGrid[i][f] = 0;
                }
            }
        }

        Users.add(new Character(6, 0, "/recursos/userSprites/Men.png", this, 100, null));
        Users.add(new Character(7, 0, "/recursos/userSprites/Bigote.png", this, 100, null));
        Users.add(new Character(7, 1, "/recursos/userSprites/Johnny.png", this, 100, null));

        Obstacule.add(new Piedra(3, 5, "/recursos/estaticoSprites/piedra.png", this));

        if(level == 1){
            enemies.add(new Zombielvl1(2, 6, "/recursos/enemigosSprites/ZombieLVL1.png", this, 100, HPEnemy_1));
            enemies.add(new Zombielvl1(5, 5, "/recursos/enemigosSprites/ZombieLVL1.png", this, 100, HPEnemy_2));
            enemies.add(new Zombielvl1(3, 3, "/recursos/enemigosSprites/ZombieLVL1.png", this, 100, HPEnemy_3));
        }else if(level == 2){
            enemies.add(new Zombielvl2(2, 6, "/recursos/enemigosSprites/ZombieLVL2.png", this, 100, HPEnemy_1));
            enemies.add(new Zombielvl1(5, 5, "/recursos/enemigosSprites/ZombieLVL1.png", this, 100, HPEnemy_2));
            enemies.add(new Zombielvl2(3, 3, "/recursos/enemigosSprites/ZombieLVL2.png", this, 100, HPEnemy_3));
        }else if(level == 3){
            enemies.add(new Zombielvl2(2, 6, "/recursos/enemigosSprites/ZombieLVL2.png", this, 100, HPEnemy_1));
            enemies.add(new Zombielvl2(5, 5, "/recursos/enemigosSprites/ZombieLVL2.png", this, 100, HPEnemy_2));
            enemies.add(new Zombielvl3(3, 3, "/recursos/enemigosSprites/ZombieLVL3.png", this, 100, HPEnemy_3));
        }else if(level == 4){
            System.exit(0);
        }
        if(level > 1){
            LevelButton.setEnabled(false);
            drawBoard();
        }
        HPEnemy_1.setBounds(525, 100, 150, 10);
        HPEnemy_1.setValue(100);
        HPEnemy_1.setBackground(Color.RED);
        HPEnemy_1.setForeground(Color.GREEN);
        add(HPEnemy_1);

        HPEnemy_2.setBounds(525, 140, 150, 10);
        HPEnemy_2.setValue(100);
        HPEnemy_2.setBackground(Color.RED);
        HPEnemy_2.setForeground(Color.GREEN);
        add(HPEnemy_2);

        HPEnemy_3.setBounds(525, 180, 150, 10);
        HPEnemy_3.setValue(100);
        HPEnemy_3.setBackground(Color.RED);
        HPEnemy_3.setForeground(Color.GREEN);
        add(HPEnemy_3);
    }

    /*
    Grafico: Coloca los botones
        inicializa las variables(Arrays principalmente)
        coloca el fondo y el tamanho de la ventana
    */
    public Grafico(){
        LevelButton = new JButton("Siguiente");
        LevelButton.setBounds(526, 460, 150, 50);
        LevelButton.addActionListener(this);
        LevelButton.setEnabled(false);
        add(LevelButton);

        AttackButton = new JButton("Atacar");
        AttackButton.setBounds(596, 20, 85, 30);
        AttackButton.addActionListener(this);
        add(AttackButton);

        Message1 = new JLabel("Zombie 1");
        Message1.setBounds(526, 65, 150, 50);
        add(Message1);
        Message2 = new JLabel("Zombie 2");
        Message2.setBounds(526, 105, 150, 50);
        add(Message2);
        Message3 = new JLabel("Zombie 3");
        Message3.setBounds(526, 145, 150, 50);
        add(Message3);

        Inventory = new ArrayList<Objetos>();
        AttackValues = new ArrayList<Integer>();
        BoardGrid = new Integer[rows][cols];
        Static_Shapes = new ArrayList<DrawingShape>();
        User_Graphics = new ArrayList<DrawingShape>();
        Users = new ArrayList<Piece>();
        enemies = new ArrayList<Piece>();
        Obstacule = new ArrayList<Piedra>();
        ObjectList = new ArrayList<Objetos>();
        DeadUser = new ArrayList<Objetos>();

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

        if (enemies.isEmpty()){
            if(level == 3){
                Message1 = new JLabel("Ya ganaste");
                Message1.setBounds(526, 300, 150, 50);
                add(Message1);
            }
            LevelButton.setEnabled(true);
        }
        //Cargar la imagen del tablero
        Image board = loadImage(board_file_path);
        Static_Shapes.add(new DrawingImage(board, new Rectangle2D.Double(0,0, board.getWidth(null), board.getHeight(null))));
        
        //Cargar la imagen de los obstaculos
        Image Piedra1 = loadImage("/recursos/estaticoSprites/piedra.png");
        Static_Shapes.add(new DrawingImage(Piedra1, new Rectangle2D.Double(Square_Width*5,
            Square_Width*3, Piedra1.getWidth(null), Piedra1.getHeight(null))));

        //Funcion moverZombie
        if((turnCounter%3 == 0 && turnCounter != 0) && !enemies.isEmpty()){
            int num = (int)(Math.random() * turnSelector);
            MoverZombie(num);
        }
        /*
        Dibuja un cuadro rojo en debajo de cada personaje
        seleccionado
        */
        if(Active_Piece != null){
            if(!enemies.contains(Active_Piece)){
                //active_Square: Cuadro rojo
                Image active_Square = loadImage(active_square_file_path);
                //casillaDisparo: Cuando se selecciona atacar, dibuja cuadros de ataque
                Image casillaDisparo = loadImage("/recursos/estaticoSprites/disparo.png");

                Static_Shapes.add(new DrawingImage(active_Square, new Rectangle2D.Double(Square_Width*Active_Piece.x,
                    Square_Width*Active_Piece.y, active_Square.getWidth(null), active_Square.getHeight(null))));

                if(Attack){
                    //Llama constructos clase Casilla
                    Casillas valor = new Casillas(Active_Piece.x, Active_Piece.y);
                    AttackValues = valor.Valores;
                    Attack = false;

                    //Carga las imagenes de cuadro de ataque
                    for(int i=0 ; i<AttackValues.size() ; i+=2){
                        Static_Shapes.add(new DrawingImage(casillaDisparo, new Rectangle2D.Double(Square_Width*AttackValues.get(i),
                            Square_Width*AttackValues.get(i+1), active_Square.getWidth(null), active_Square.getHeight(null))));
                    }
                }
            }
        }else{
            AttackButton.setEnabled(false);
        }
        for(int i=0 ; i<Users.size() ; i++){
            int COL = Users.get(i).x;
            int ROW = Users.get(i).y;

            //Carga las imagenes de cada personaje
            Image piece = loadImage(Users.get(i).file_path);
            User_Graphics.add(new DrawingImage(piece, new Rectangle2D.Double(Square_Width*COL, 
                Square_Width*ROW, piece.getWidth(null), piece.getHeight(null))));
        }

        for(int i=0 ; i<enemies.size() ; i++){
            int COL = enemies.get(i).x;
            int ROW = enemies.get(i).y;

            //Carga las imagenes de cada enemigos
            Image piece = loadImage(enemies.get(i).file_path);
            User_Graphics.add(new DrawingImage(piece, new Rectangle2D.Double(Square_Width*COL, 
                Square_Width*ROW, piece.getWidth(null), piece.getHeight(null))));
        }
        if(!ObjectList.isEmpty()){
            //Carga las imagenes de los obj. dropeados
            Image Objeto = loadImage("/recursos/estaticoSprites/Objeto1.png");
            for(int i=0 ; i<ObjectList.size() ; i++){
                Static_Shapes.add(new DrawingImage(Objeto, new Rectangle2D.Double(Square_Width*ObjectList.get(i).x,
                Square_Width*ObjectList.get(i).y, Objeto.getWidth(null), Objeto.getHeight(null))));
            }
        }
        if(!DeadUser.isEmpty()){
            //Carga las imagenes de las lapidas
            Image lapida = loadImage("/recursos/estaticoSprites/Lapida.png");
            for(int i=0 ; i<DeadUser.size() ; i++){
                Static_Shapes.add(new DrawingImage(lapida, new Rectangle2D.Double(Square_Width*DeadUser.get(i).x,
                Square_Width*DeadUser.get(i).y, lapida.getWidth(null), lapida.getHeight(null))));
            }
        }
        if(!Inventory.isEmpty()){
            int NumTemp = 0, rest = 0;
            Image objObtenido = loadImage("/recursos/estaticoSprites/Objeto1.png");
            for(int i=0 ; i<Inventory.size() ; i++){
                if(NumTemp != 0 && NumTemp%5 == 0){
                    NumTemp ++;
                    rest = i-1;
                }
                Static_Shapes.add(new DrawingImage(objObtenido, new Rectangle2D.Double(Inventory.get(i-rest).x,
                Inventory.get(NumTemp).y, 29, 37)));
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
        for(int i=0 ; i<enemies.size() ; i++){
            Active_Piece = enemies.get(i);
            if(Active_Piece.closePiece(Active_Piece.x , Active_Piece.y)){
                Active_Piece.MoveEnemy();

            }else{
                Users.remove(getPiece(Piece.x_User, Piece.y_User));
                DeadUser.add(new Objetos(Piece.x_User, Piece.y_User, "/recursos/estaticoSprites/Lapida.png"));
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
        for(Piece p : enemies){
            if(p.x == x && p.y == y){
                return p;
            }
        }
        return null;
    }

    //Metodo para obtener un obstaculo, devuelve tipo Piedra
    public Piedra getObstaculo(int x, int y){
        for(Piedra a : Obstacule){
            if(a.x == x && a.y == y){
                return a;
            }
        }
        return null;
    }

    public Objetos getObjetos(int x, int y){
        for(Objetos obj : ObjectList){
            if(obj.x == x && obj.y == y){
                return obj;
            }
        }
        return null;
    }

    @Override
    //Activar funcionamiento a los botones
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == LevelButton){
            Static_Shapes.clear();
            User_Graphics.clear();
            Users.clear();
            enemies.clear();
            Obstacule.clear();
            turnSelector = 3;
            level++;
            initGrid();
        }
        if (e.getSource() == AttackButton){
            AttackButton.setEnabled(false);
            Attack = true;
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

            if(false == enemies.contains(clicked_piece) && clicked_piece != null){
                //Permite seleccionar el personaje y guardarlo en Active_Piece

                Active_Piece = clicked_piece;
                Selected_User = true;
                AttackButton.setEnabled(true);
            }else if(Selected_User == true && Clicked_Column < 8){
                if((clicked_obstacule == null) && Move){
                    if(Active_Piece.canMove(Clicked_Row, Clicked_Column) && Users.contains(clicked_piece) == false){
                        
                        //Mueve el personaje a la posicion seleccionada
                        Active_Piece.x = Clicked_Column;
                        Active_Piece.y = Clicked_Row;

                        turnCounter ++;
                        Selected_User = false;

                        Objetos objetoDropeado = getObjetos(Clicked_Column, Clicked_Row);
                        if(objetoDropeado != null){

                            Active_Piece.IncreaseDamage();
                            Inventory.add(new Objetos(530+(Inventory.size()*29), 265+(((int)(Inventory.size()/4))*37), "/recursos/estaticoSprites/Objeto1.png"));
                            ObjectList.remove(objetoDropeado);
                        }
                        Active_Piece = null;
                    }
                }else{

                    //Permite seleccionar casillas para atacar
                    Move = true;
                    if(Active_Piece.UserAttack(Active_Piece.x, Active_Piece.y, Clicked_Column, Clicked_Row)){
                        if (enemies.contains(clicked_piece)){
                            clicked_piece.ReceiveDamage(clicked_piece);
                            clicked_piece.SaludBarra.setValue(clicked_piece.Salud);
                            if(clicked_piece.Salud <= 0){
                                enemies.remove(clicked_piece);
                                turnSelector--;
                                ObjectList.add(new Objetos(Clicked_Column, Clicked_Row, 
                                    "/recursos/estaticoSprites/Objeto1.png"));
                            }
                            turnCounter ++;
                        }
                    }
                    Active_Piece = null;
                    Selected_User = false;
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
        drawInventory(g2);
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
    private void drawInventory(Graphics g2){
        g2.setColor(Color.GREEN);
        g2.fillRect(530, 265, 145, 185);
        g2.setColor(Color.BLACK);
        for(int a=0 ; a<5 ; a++){
            for(int b=0 ; b<5 ; b++){
                g2.drawRect(530+(29*a), 265+(37*b), 29, 37);
            }
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

