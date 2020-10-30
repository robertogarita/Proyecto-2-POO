package pruebas;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.*;
import java.io.IOException;

public class Grafico extends JPanel{
    
    BufferedImage imagen, imagenClickeada;
    public int x,y;
    public boolean clickeado = false;

    public void cargarImagenes(){
        try{
            imagen = ImageIO.read(getClass().getResourceAsStream("/recursos/fuego.jpg"));
            imagenClickeada = ImageIO.read(getClass().getResourceAsStream("/recursos/agua.jpg"));
        }catch(IOException E){
            E.printStackTrace();
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for(int f=0 ; f<5; f++){
            for(int c=0 ; c<5 ; c++){
                if(f == x && c == y && clickeado == true) {
                    g.drawImage(imagenClickeada, 100*f, 100*c, 100, 100, null);    
                }else{
                    g.drawImage(imagen, 100*f, 100*c, 100, 100, null);
                }
            }
        }
    }
}

