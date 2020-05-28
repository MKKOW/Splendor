package client;

import javax.swing.*;
import java.awt.*;

public class Deck extends JComponent {
    private JPanel deck;
    private JLabel amountField;
    private JLabel levelField;
    public void set(int amount,int level){
        deck.setToolTipText(Integer.toString(amount));
        levelField.setText(Integer.toString(level));
    }
    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        deck.setBackground(bg);
    }
}
