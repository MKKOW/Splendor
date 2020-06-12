package gui;

import Controller.BoardMaker;
import Model.ClientBoard;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import server.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.IOException;

public class Connect extends JPanel {
    private int Width;
    private int Height;
    private JPanel field;
    private JLabel titleLabel;
    private JLabel hostLabel;
    private JLabel portLabel;
    private TextArea hostArea;
    private TextArea nickArea;
    private TextArea portArea;
    private JButton connect;
    private JButton play;
    private JButton back;
    private int port;
    private String host;
    private JFrame frame;
    private Client client;
    private AffineTransform affinetransform;
    private FontRenderContext frc;
    private Font font;
    private int fontHeight;
    private int labelsWidth;
    private int buttonWidth;
    public Connect(JFrame jframe){
        super(null);
        frame=jframe;
        setBackground(Color.BLACK);
        Width=frame.getWidth();
        Height=frame.getHeight();
        field = new JPanel(null);
        field.setBackground(Color.LIGHT_GRAY);
        field.setSize(Width/3,Height/6);
        field.setLocation(Width/3,Height/3);
        add(field);
        setConnect();
    }
    private void setConnect(){
        titleLabel=new JLabel("Connect to a server");
        fontHeight = field.getHeight()/8;
        font = new Font(titleLabel.getFont().getFontName(),titleLabel.getFont().getStyle(),fontHeight);
        // needed for figuring out the width of string
        affinetransform = new AffineTransform();
        frc = new FontRenderContext(affinetransform,true,true);

        setTitleLabel("Connect to a server");
        titleLabel.setFont(font);
        field.add(titleLabel);
        hostLabel=new JLabel("Host:");
        portLabel=new JLabel("Port:");
        labelsWidth=Math.max((int) font.getStringBounds(hostLabel.getText(),frc).getWidth(),(int) font.getStringBounds(portLabel.getText(),frc).getWidth());
        hostLabel.setFont(font);
        portLabel.setFont(font);
        hostLabel.setSize(labelsWidth,fontHeight);
        portLabel.setSize(labelsWidth,fontHeight);
        hostLabel.setLocation(labelsWidth/2,fontHeight*3);
        portLabel.setLocation(labelsWidth/2,fontHeight*5);
        field.add(hostLabel);
        field.add(portLabel);
        hostArea=new TextArea("",1,0,TextArea.SCROLLBARS_NONE);
        portArea=new TextArea("",1,0,TextArea.SCROLLBARS_NONE);
        hostArea.setSize(field.getWidth()-labelsWidth*3,fontHeight*3/2);
        portArea.setSize(field.getWidth()-labelsWidth*3,fontHeight*3/2);
        hostArea.setLocation(labelsWidth*2,fontHeight*3-hostArea.getHeight()/4);
        portArea.setLocation(labelsWidth*2,fontHeight*5-portArea.getHeight()/4);
        hostArea.setFont(font);
        portArea.setFont(font);
        field.add(hostArea);
        field.add(portArea);
        connect=new JButton("Connect");
        back=new JButton("Back");
        buttonWidth = Math.max((int) font.getStringBounds(connect.getText(),frc).getWidth(),(int) font.getStringBounds(back.getText(),frc).getWidth());
        connect.setLocation(field.getWidth()-buttonWidth,field.getHeight()-fontHeight);
        connect.setSize(buttonWidth,fontHeight);
        field.add(connect);

        back.setLocation(0,field.getHeight()-fontHeight);
        back.setSize(buttonWidth,fontHeight);
        field.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                frame.setContentPane(new Menu(frame).view);
            }
        });
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    host = hostArea.getText();
                    port = Integer.parseInt(portArea.getText());
                    //Client client=new Client(port,host);
                    //client.runClient();
                    clear();
                    setNick();
                    //<temporary>
                    //ClientBoard board=BoardMaker.generatePresentationBoard();
                    //ObjectMapper mapper = new ObjectMapper();
                    //String json = mapper.writeValueAsString(board);
                    //JSONObject object=new JSONObject(json);
                    //<\temporary>
                    //frame.setContentPane(new Game(frame,object));
                    //frame.setContentPane(new Game(frame,object,));
                }
                catch (Exception exception){
                    setVisible(true);
                    setTitleLabel("Could not connect to that server!");
                }
            }
        });
    }
    private void setNick(){
        frame.repaint();
        setTitleLabel("Set your nickname:");
        field.add(titleLabel);
        nickArea=new TextArea("",1,0,TextArea.SCROLLBARS_NONE);
        nickArea.setSize(field.getWidth()*3/4,fontHeight);
        nickArea.setLocation(field.getWidth()/8,fontHeight*3);
        field.add(nickArea);
        play=new JButton("Play");
        play.setSize(buttonWidth,fontHeight);
        play.setLocation((field.getWidth()-play.getWidth()),fontHeight*5);
        field.add(play);
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              //  try {
                    setVisible(false);
                    String nick = nickArea.getText();
                    //<temporary>
                try {
                    ClientBoard.setInstanceFromServerBoard(BoardMaker.generateRandomServerBoard(4));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                ClientBoard board= ClientBoard.getInstance();
                System.out.println(board);
                ObjectMapper mapper = new ObjectMapper();
                String json = null;
                try {
                    json = mapper.writeValueAsString(board);
                } catch (JsonProcessingException jsonProcessingException) {
                    jsonProcessingException.printStackTrace();
                }
                JSONObject object=new JSONObject(json);
                    //<\temporary>
                try {
                    frame.setContentPane(new Game(frame,object,nick));
                } catch (JsonProcessingException jsonProcessingException) {
                    jsonProcessingException.printStackTrace();
                }
                //frame.setContentPane(new Game(frame,object,client));
               // }
              //  catch (Exception exception){
              //      setVisible(true);
               //     setTitleLabel("Could not connect to that server!");
               // }
            }
        });
    }
    private void clear(){
        field.removeAll();
        connect.setVisible(false);
        back.setVisible(false);
    }
    private void setTitleLabel(String title){
        titleLabel.setText(title);
        int titleWidth =(int) font.getStringBounds(titleLabel.getText(),frc).getWidth();
        titleLabel.setLocation((field.getWidth()-titleWidth)/2,fontHeight);
        titleLabel.setSize(titleWidth,fontHeight);
    }
}
