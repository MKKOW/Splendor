package client;

import javax.swing.*;
import java.awt.*;

public class coinStack extends JComponent {
    private JLabel amountField;
    private JPanel coin;
    private int amount;
    private String textColor;
    public void set(int amount, String textColor, Color background){
        if(amount==0)
            setVisible(false);
        this.amount=amount;
        this.textColor=textColor;
        setBackground(background);
        amountField.setText("<html><font color='"+textColor+"'>"+ amount +"</font></html>");
    }
    public void setAmount(int amountField){
        if(amount==0)
            setVisible(false);
        this.amountField.setText("<html><font color='"+textColor+"'>"+ amountField +"</font></html>");
    }
    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        coin.setBackground(bg);
        amountField.setBackground(bg);
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        coin.setVisible(aFlag);
        amountField.setVisible(aFlag);
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor=textColor;
        String buffer= amountField.getText();
        buffer=buffer.replaceFirst("color='"+textColor+"'","color='"+textColor+"'");
        amountField.setText(buffer);
    }
}
