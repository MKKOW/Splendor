package client;

import Controller.BoardMaker;
import Exceptions.InactivePlayersException;
import Model.ClientBoard;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GUI for the menu section
 */
public class Menu{
    private JButton playButton;
    private JButton settingsButton;
    private JButton exitButton;
    private JLabel title;
    protected JPanel view;


    public Menu(JFrame frame){
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ClientBoard clientBoard= BoardMaker.generatePresentationBoard();
                    view.setVisible(false);
                    ObjectMapper mapper=new ObjectMapper();
                    mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                    String json =mapper.writeValueAsString(clientBoard);
                    JSONObject board=new JSONObject(json);
                    frame.setContentPane(new Game(frame,board).view);
                } catch (InactivePlayersException | JsonProcessingException inactivePlayersException) {
                    inactivePlayersException.printStackTrace();
                }

            }
        });
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.setVisible(false);
                frame.setContentPane(new Settings(frame).view);
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}
