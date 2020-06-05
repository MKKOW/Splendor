package gui;

import javax.swing.*;
import java.awt.*;

public class coinStack extends JPanel {
    private JLabel amountField=new JLabel();
    private int amount;
    private String textColor;
    public coinStack(){
        super(new GridLayout());
        this.add(amountField);
    }
    public coinStack(int amount, String textColor, Color background){
        super(new GridLayout());
        set(amount,textColor,background);
        this.add(amountField);
    }
    public void set(int amount, String textColor, Color background){
        if(amount==0)
            setVisible(false);
        else
            setVisible(true);
        this.amount=amount;
        this.textColor=textColor;
        setBackground(background);
        amountField.setText("<html><font color='"+textColor+"'>"+ amount +"</font></html>");
    }
    public void setAmount(int amountField){
        amount=amountField;
        if(amount==0)
            setVisible(false);
        else
            setVisible(true);
        this.amountField.setText("<html><font color='"+textColor+"'>"+ amountField +"</font></html>");
    }

    public int getAmount() {
        return amount;
    }
    @Override
    public void setSize(int width,int height){
        resize(width,height);
        String fontName=amountField.getFont().getFontName();
        int style=amountField.getFont().getStyle();
        amountField.setFont(new Font(fontName,style,height));
    }
    public String getTextColor() {
        return textColor;
    }
    public void forceVisible(){
        setVisible(true);
        amountField.setVisible(true);
    }
    public void setTextColor(String textColor) {
        this.textColor=textColor;
        String buffer= amountField.getText();
        buffer=buffer.replaceFirst("color='"+textColor+"'","color='"+textColor+"'");
        amountField.setText(buffer);
    }
}
