package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game {
    protected JPanel view;
    private Card Lord1Card;
    private Card Lord2Card;
    private Card Lord3Card;
    private Card Lord4Card;
    private Card Lord5Card;
    private Card Blue1;
    private Card Blue2;
    private Card Blue3;
    private Card Blue4;
    private Card Yellow1;
    private Card Yellow2;
    private Card Yellow3;
    private Card Yellow4;
    private Card Green1;
    private Card Green2;
    private Card Green3;
    private Card Green4;
    private JButton Menu;
    private Card[] LordCards;
    private Card[] BlueCards;
    private Card[] YellowCards;
    private Card[] GreenCards;
    public Game(JFrame frame) {
        LordCards=new Card[5];
        LordCards[0]=Lord1Card;
        LordCards[1]=Lord2Card;
        LordCards[2]=Lord3Card;
        LordCards[3]=Lord4Card;
        LordCards[4]=Lord5Card;
        BlueCards=new Card[4];
        BlueCards[0]=Blue1;
        BlueCards[1]=Blue2;
        BlueCards[2]=Blue3;
        BlueCards[3]=Blue4;
        YellowCards=new Card[4];
        YellowCards[0]=Yellow1;
        YellowCards[1]=Yellow2;
        YellowCards[2]=Yellow3;
        YellowCards[3]=Yellow4;
        GreenCards=new Card[4];
        GreenCards[0]=Green1;
        GreenCards[1]=Green2;
        GreenCards[2]=Green3;
        GreenCards[3]=Green4;
        for(Card card:BlueCards)
            card.setBackground(Color.BLUE);
        for(Card card:YellowCards)
            card.setBackground(Color.YELLOW);
        for(Card card:GreenCards)
            card.setBackground(Color.GREEN);
        Menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.setVisible(false);
                frame.setContentPane(new Menu(frame).view);
            }
        });
    }

}
