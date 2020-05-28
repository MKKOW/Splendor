package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
    private Deck BlueDeck;
    private Deck YellowDeck;
    private Deck GreenDeck;
    private coinStack whiteStack;
    private coinStack blueStack;
    private coinStack greenStack;
    private coinStack redStack;
    private coinStack blackStack;
    private coinStack goldStack;
    private JPanel coinField;
    private Card[] LordCards;
    private Card[] BlueCards;
    private Card[] YellowCards;
    private Card[] GreenCards;
    private Card[] allCards;
    public Game(JFrame frame) {
        assign();
        whiteStack.set(10,"black",Color.WHITE);
        blueStack.set(10,"white",Color.BLUE);
        greenStack.set(10,"black",Color.GREEN);
        redStack.set(10,"black",Color.RED);
        blackStack.set(10,"white",Color.BLACK);
        goldStack.set(10,"white",Color.ORANGE);
        BlueDeck.setBackground(Color.BLUE);
        YellowDeck.setBackground(Color.YELLOW);
        GreenDeck.setBackground(Color.GREEN);
        BlueDeck.set(10,1);
        YellowDeck.set(20,2);
        GreenDeck.set(30,3);

        for(Card card:BlueCards)
            card.setBackground(Color.BLUE);
        for(Card card:YellowCards)
            card.setBackground(Color.YELLOW);
        for(Card card:GreenCards)
            card.setBackground(Color.GREEN);
        for(Card card:BlueCards) {
            card.setCost(0, 2, 3, 1, 0);
            card.setPoints(0);
        }
        for(Card card:YellowCards) {
            card.setCost(2, 3, 0, 0, 0);
            card.setPoints(2);
        }
        for(Card card:GreenCards) {
            card.setCost(0, 0, 0, 0, 0);
            card.setPoints(3);
        }
        for(Card card:LordCards){
            card.setCost(5,5,5,5,5);
            card.setPoints(5);
        }
        LordCards[3].setPoints(3);
        /*Dimension dimension=new Dimension(50,100);
        BlueDeck.setPreferredSize(dimension);
        BlueDeck.setMaximumSize(dimension);
        BlueDeck.setMinimumSize(dimension);
        YellowDeck.setPreferredSize(dimension);
        YellowDeck.setMaximumSize(dimension);
        YellowDeck.setMinimumSize(dimension);
        GreenDeck.setPreferredSize(dimension);
        GreenDeck.setMaximumSize(dimension);
        GreenDeck.setMinimumSize(dimension);
        for (Card card:allCards){
            card.setPreferredSize(dimension);
            card.setMaximumSize(dimension);
            card.setMinimumSize(dimension);
        }*/
        Menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.setVisible(false);
                frame.setContentPane(new Menu(frame).view);
            }
        });
    }
    private void assign(){
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
        allCards=new Card[17];
        int i=0;
        for(Card card:LordCards){
            allCards[i]=card;i++;}
        for(Card card:BlueCards){
            allCards[i]=card;i++;}
        for(Card card:YellowCards){
            allCards[i]=card;i++;}
        for(Card card:GreenCards){
            allCards[i]=card;i++;}
    }
}
