package gui;

import Controller.BoardMaker;
import Exceptions.InactivePlayersException;
import Model.ClientBoard;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
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
    private int Width;
    private int Height;
    public Menu(JFrame frame){
        Width=frame.getWidth();
        Height=frame.getHeight();
        view=new JPanel(null);
        ImageIcon icon=new ImageIcon("src/main/resources/logo.gif");
        int imageHeight=icon.getIconHeight();
        int imageWidth=icon.getIconWidth();
        title=new JLabel(icon);
        playButton=new JButton("Play");
        settingsButton=new JButton("Settings");
        exitButton=new JButton("Exit");
        view.setBackground(Color.BLACK);
        view.add(title);
        view.add(playButton);
        view.add(settingsButton);
        view.add(exitButton);
        title.setSize(imageWidth,imageHeight);
        title.setLocation(Width/2-title.getWidth()/2,Height/6-title.getHeight()/2);

        playButton.setSize(Width/3,Height/12);
        playButton.setLocation(Width/2-playButton.getWidth()/2,Height*2/6-playButton.getHeight()/2);

        settingsButton.setSize(Width/3,Height/12);
        settingsButton.setLocation(Width/2-settingsButton.getWidth()/2,Height*3/6-settingsButton.getHeight()/2);

        exitButton.setSize(Width/3,Height/12);
        exitButton.setLocation(Width/2-exitButton.getWidth()/2,Height*4/6-exitButton.getHeight()/2);

        frame.add(view);
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
                    frame.setContentPane(new Game(frame,board));
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
