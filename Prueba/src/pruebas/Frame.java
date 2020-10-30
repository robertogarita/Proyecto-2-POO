package pruebas;

import javax.swing.JFrame;
import java.awt.event.*;

public class Frame extends JFrame implements MouseListener{

    Grafico ventana = new Grafico();

    public Frame(){
        ventana.cargarImagenes();
        this.setSize(500,500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(ventana);
        this.setVisible(true);
        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        ventana.clickeado = true;
        int numX = e.getX();
        int numY = e.getY();
        double x = (numX/Math.pow(10, 2));
        double y = (numY/Math.pow(10, 2));
        ventana.x = (int)x;
        ventana.y = (int)y;
        this.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }
    
}
