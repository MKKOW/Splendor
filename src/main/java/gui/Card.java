package gui;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

public class Card extends JPanel{
    private coinStack[] costs;//white, blue, green, red ,black
    private coinStack[] discount;//white, blue, green, red ,black
    private coinStack pointsField;
    private JPanel costField;
    private JPanel discountField;
    private JLabel textField;
    private int points;
    private int X;
    private int Y;
    private int id;
    private int width;
    private int height;
    private int stackWidth;
    private int stackHeight;
    private Point initialPoint;
    public Card(JSONObject card,boolean noble,int width,int height,int x,int y,String text){
        this(card.getInt("id"),
                card.getJSONObject("cost").getInt("white"),
                card.getJSONObject("cost").getInt("blue"),
                card.getJSONObject("cost").getInt("green"),
                card.getJSONObject("cost").getInt("red"),
                card.getJSONObject("cost").getInt("black"),
                0,
                noble?text.equals("Claimed Card")?card.getJSONObject("discount").getInt("white"):0:card.getJSONObject("discount").getInt("white"),
                noble?text.equals("Claimed Card")?card.getJSONObject("discount").getInt("blue"):0:card.getJSONObject("discount").getInt("blue"),
                noble?text.equals("Claimed Card")?card.getJSONObject("discount").getInt("green"):0:card.getJSONObject("discount").getInt("green"),
                noble?text.equals("Claimed Card")?card.getJSONObject("discount").getInt("red"):0:card.getJSONObject("discount").getInt("red"),
                noble?text.equals("Claimed Card")?card.getJSONObject("discount").getInt("black"):0:card.getJSONObject("discount").getInt("black"),
                card.getInt("prestige"),
                noble,
                width,
                height,
                x,
                y,
                text);
    }
    public Card(int id,int costWhite,int costBlue,int costGreen,int costRed,int costBlack,int costYellow,
                int discountWhite,int discountBlue,int discountGreen,int discountRed,int discountBlack,
                int points,boolean noble,int width,int height,int x, int y,String text) {
        super(null);
        setBackground(Color.RED);
        costs=new coinStack[6];
        discount=new coinStack[5];
        pointsField=new coinStack(points,"black",Color.WHITE);
        costField=new JPanel(null);
        discountField=new JPanel(null);
        textField=new JLabel(text);
        for(int i=0;i<6;i++) {
            costs[i] = new coinStack();
        }
        for(int i=0;i<5;i++){
            discount[i]=new coinStack();
        }
        this.id=id;
        this.points=points;
        X=x;
        Y=y;
        this.width=width;
        this.height=height;
        initialPoint=new Point(X,Y);
        set(costWhite,costBlue,costGreen,costRed,costBlack,costYellow,discountWhite,discountBlue,discountGreen,discountRed,discountBlack);
        setPoints(points);
        setSize(width,height);
        setLocation(x,y);
        int i=0;
        stackWidth=width/7;
        stackHeight=height/7;
        for(coinStack stack:costs){
            if(stack.isVisible()) {
                costField.add(stack);
                stack.setSize(stackWidth, stackHeight);
                stack.setLocation(0, stackHeight * i);
                i++;
            }
        }
        int j=0;
        for(coinStack stack:discount){
            if(stack.isVisible()) {
                discountField.add(stack);
                stack.setSize(stackWidth, stackHeight);
                stack.setLocation(stackWidth * j,0 );
                j++;
            }
        }
        add(costField);
        costField.setSize(stackWidth,i*stackHeight);
        costField.setLocation(0,height-i*stackHeight);
        add(discountField);
        discountField.setSize(stackWidth*j,stackHeight);
        discountField.setLocation(width-j*stackWidth,0);
        add(pointsField);
        pointsField.setSize(stackWidth,stackHeight);
        pointsField.setLocation(0,0);
        add(textField);
        textField.setSize(stackWidth*5,stackHeight);
        textField.setLocation(stackWidth,stackHeight);
        /*if(!noble) {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    X = e.getX();
                    Y = e.getY();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    setLocation(initialPoint);
                }
            });
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    int dx = e.getX() - X;
                    int dy = e.getY() - Y;
                    super.mouseDragged(e);
                    setLocation(getX() + dx, getY() + dy);
                }
            });
        }
        */
    }

    public void setPoints(int points){
        this.points=points;
        pointsField.setAmount(points);
    }
    public void set(int White,int Blue,int Green, int Red,int Black,int Yellow,
                    int WhiteDis,int BlueDis,int GreenDis, int RedDis,int BlackDis){
        costs[0].set(White,"black",Color.WHITE);
        costs[1].set(Blue,"white",Color.BLUE);
        costs[2].set(Green,"black",Color.GREEN);
        costs[3].set(Red,"black",Color.RED);
        costs[4].set(Black,"white",Color.BLACK);
        costs[5].set(Yellow,"black",Color.YELLOW);
        discount[0].set(WhiteDis,"black",Color.WHITE);
        discount[1].set(BlueDis,"white",Color.BLUE);
        discount[2].set(GreenDis,"black",Color.GREEN);
        discount[3].set(RedDis,"black",Color.RED);
        discount[4].set(BlackDis,"white",Color.BLACK);
        if(White!=0||Blue!=0||Green!=0||Red!=0||Black!=0) {
            setVisible(true);
        }
    }
    public void forceVisible(){
        setVisible(true);
            costField.removeAll();
            discountField.removeAll();
            pointsField.forceVisible();
            int i=0;
            for(coinStack stack:costs){
                    stack.forceVisible();
                    costField.add(stack);
                    stack.setSize(stackWidth, stackHeight);
                    stack.setLocation(0, stackHeight * i);
                    i++;
            }
            int j=0;
            for(coinStack stack:discount){
                    stack.forceVisible();
                    discountField.add(stack);
                    stack.setSize(stackWidth, stackHeight);
                    stack.setLocation(stackWidth * j,0 );
                    j++;
            }
            add(costField);
            costField.setSize(stackWidth,i*stackHeight);
            costField.setLocation(0,height-i*stackHeight);
            add(discountField);
            discountField.setSize(stackWidth*j,stackHeight);
            discountField.setLocation(width-j*stackWidth,0);
            add(pointsField);
            pointsField.setSize(stackWidth,stackHeight);
            pointsField.setLocation(0,0);
    }

    public int getId() {
        return id;
    }
    public coinStack[] getCosts(){
        return costs;
    }
}
