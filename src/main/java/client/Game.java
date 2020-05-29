package client;

import Model.ClientBoard;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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
    public Game(JFrame frame, JSONObject board) throws JsonProcessingException {
        assign();
        //visual settings
        BlueDeck.setBackground(Color.BLUE);
        YellowDeck.setBackground(Color.YELLOW);
        GreenDeck.setBackground(Color.GREEN);
        for(Card card:BlueCards) {
            card.setBackground(Color.BLUE);
        }
        for(Card card:YellowCards) {
            card.setBackground(Color.YELLOW);
        }
        for(Card card:GreenCards) {
            card.setBackground(Color.GREEN);
        }
        loadBoard(board);
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
    private void loadBoard(JSONObject board){
        //load from json
        BlueDeck.set(10,1);
        YellowDeck.set(20,2);
        GreenDeck.set(30,3);

        whiteStack.set(board.getJSONObject("bankCash").getInt("white"),"black",Color.WHITE);
        blueStack.set(board.getJSONObject("bankCash").getInt("blue"),"white",Color.BLUE);
        greenStack.set(board.getJSONObject("bankCash").getInt("green"),"black",Color.GREEN);
        redStack.set(board.getJSONObject("bankCash").getInt("red"),"black",Color.RED);
        blackStack.set(board.getJSONObject("bankCash").getInt("black"),"white",Color.BLACK);
        goldStack.set(board.getJSONObject("bankCash").getInt("yellow"),"white",Color.ORANGE);
        JSONArray[] CardArray=new JSONArray[3];
        for(int j=0;j<3;j++){
            CardArray[j]= board.
                    getJSONObject("developmentCardsOnBoard").
                    getJSONArray("level"+(j+1));
            for(int i=0;i<CardArray[j].length();i++) {
                Card card=allCards[5+4*j+i];
                JSONObject jsonCard=CardArray[j].getJSONObject(i);
                JSONObject cost=jsonCard.
                        getJSONObject("cost");
                card.setCost(cost.getInt("white"),
                        cost.getInt("blue"),
                        cost.getInt("green"),
                        cost.getInt("red"),
                        cost.getInt("black"));
                card.setPoints(jsonCard.
                        getInt("prestige"));
            }
        }
        JSONArray nobles=board.getJSONObject("nobles").getJSONArray("nobles");
        for(int i=0;i<nobles.length();i++){
            Card card=LordCards[i];
            JSONObject jsonLord=nobles.getJSONObject(i);
            JSONObject cost=jsonLord.getJSONObject("cost");
            card.setCost(cost.getInt("white"),
                    cost.getInt("blue"),
                    cost.getInt("green"),
                    cost.getInt("red"),
                    cost.getInt("black"));
            card.setPoints(jsonLord.
                    getInt("prestige"));
        }
        LordCards[3].setPoints(3);
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
