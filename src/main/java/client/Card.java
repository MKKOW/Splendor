package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Card extends JComponent{
    private JPanel Card;
    private JPanel bonus;
    private JPanel points;
    private coinStack costWhite;
    private coinStack costBlue;
    private coinStack costGreen;
    private coinStack costRed;
    private coinStack costBlack;
    private JPanel costField;
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
    public void setCost(int White,int Blue,int Green, int Red,int Black){
        costWhite.set(White,"black",Color.WHITE);
        costBlue.set(Blue,"white",Color.BLUE);
        costGreen.set(Green,"black",Color.GREEN);
        costRed.set(Red,"black",Color.RED);
        costBlack.set(Black,"white",Color.BLACK);
        if(White!=0||Blue!=0||Green!=0||Red!=0||Black!=0)
            costField.setVisible(true);
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        Card.setBackground(bg);
    }
}
