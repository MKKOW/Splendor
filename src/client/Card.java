package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Card extends JComponent{
    private JPanel Card;
    private int x;
    private int y;
    private Point initialPoint;
    public Card() {
        Card.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                x=e.getX();
                y=e.getY();
                if(initialPoint==null)
                    initialPoint=Card.getLocationOnScreen();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                Card.setLocation(initialPoint);
            }
        });
        Card.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int dx =e.getX()-x;
                int dy =e.getY()-y;
                super.mouseDragged(e);
                Card.setLocation(Card.getX()+dx,Card.getY()+dy);
            }
        });
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        Card.setBackground(bg);
    }
}
