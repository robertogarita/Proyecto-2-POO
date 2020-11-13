package pruebas;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Component;

/*
Clase que genera la ventana principal
*/

public class Frame extends JFrame{

    public Frame(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        Component component = new Grafico();
        add(component, BorderLayout.CENTER);

        setLocation(200, 50);
        setSize(700,560);
        setVisible(true);
    }
}
