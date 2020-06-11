package gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

/**
 * GUI for the settings section
 */
public class Settings {
    public static final String configPath = "src/main/resources/.config";
    private JButton applyButton;
    protected JPanel view;
    private JButton returnButton;
    private JSlider HeightSlider;
    private JLabel ResolutionHeight;
    private JSlider WidthSlider;
    private JLabel ResolutionWidth;
    private JCheckBox fullscreenCheckBox;
    private JLabel fullscreenBox;
    private int Width;
    private int Height;
    public Settings(JFrame frame) {
        Width=frame.getWidth();
        Height=frame.getHeight();
        applyButton=new JButton("Apply");
        view=new JPanel(null);
        returnButton=new JButton("Return");
        HeightSlider=new JSlider();
        ResolutionHeight=new JLabel();
        WidthSlider=new JSlider();
        ResolutionWidth=new JLabel();
        fullscreenCheckBox=new JCheckBox();
        fullscreenBox=new JLabel("<html><font color='white'>Fullscreen</font></html>");
        view.setBackground(Color.BLACK);
        view.add(applyButton);
        view.add(returnButton);
        view.add(HeightSlider);
        view.add(WidthSlider);
        view.add(fullscreenBox);
        view.add(fullscreenCheckBox);
        view.add(ResolutionHeight);
        view.add(ResolutionWidth);
        frame.add(view);
        Dimension textBoxDim=new Dimension(Width/6,12);
        int firstColumn=Width/3;
        int secondColumn=firstColumn+Width/12;
        ResolutionHeight.setSize(textBoxDim);
        ResolutionHeight.setLocation(firstColumn,Height/3+20);
        ResolutionWidth.setSize(textBoxDim);
        ResolutionWidth.setLocation(firstColumn,Height/3);
        HeightSlider.setSize(textBoxDim.width*2,textBoxDim.height);
        HeightSlider.setLocation(secondColumn,Height/3+20);
        WidthSlider.setSize(textBoxDim.width*2,textBoxDim.height);
        WidthSlider.setLocation(secondColumn,Height/3);
        fullscreenBox.setSize(textBoxDim);
        fullscreenBox.setLocation(firstColumn,Height/3+40);
        fullscreenCheckBox.setSize(20,20);
        fullscreenCheckBox.setLocation(secondColumn,Height/3+40);
        applyButton.setSize(textBoxDim.width*2,40);
        applyButton.setLocation(secondColumn,Height/3+65);
        returnButton.setSize(textBoxDim.width/2-5,40);
        returnButton.setLocation(firstColumn,Height/3+65);
        Toolkit tk =Toolkit.getDefaultToolkit();
        Dimension screenSize=tk.getScreenSize();
        ResolutionWidth.setText("<html><font color='white'>Resolution Width: " + frame.getWidth()+"</font></html>");
        ResolutionHeight.setText("<html><font color='white'>Resolution Height: " + frame.getHeight()+"</font></html>");
        WidthSlider.setMaximum(screenSize.width);
        HeightSlider.setMaximum(screenSize.height);
        WidthSlider.setValue(frame.getWidth());
        HeightSlider.setValue(frame.getHeight());
        fullscreenCheckBox.setSelected(frame.isUndecorated());
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.setVisible(false);
                frame.setContentPane(new Menu(frame).view);
            }
        });
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int WIDTH = WidthSlider.getValue();
                int HEIGHT = HeightSlider.getValue();
                boolean FULLSCREEN = fullscreenCheckBox.isSelected();
                String string = "";
                string += "width = " + WIDTH+System.lineSeparator();
                string += "height = " + HEIGHT+System.lineSeparator();
                string += "fullscreen = " + FULLSCREEN+System.lineSeparator();
                FileWriter fileWriter = null;
                try {
                    fileWriter = new FileWriter(configPath);
                    fileWriter.write(string);
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                view.setVisible(false);
                frame.setContentPane(new Menu(frame).view);
            }
        });
        WidthSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(!fullscreenCheckBox.isSelected()) {
                    ResolutionWidth.setText("<html><font color='white'>Resolution Width: " + WidthSlider.getValue()+"</font></html>");
                }else{
                    WidthSlider.setValue(screenSize.width);
                }
            }
        });
        HeightSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(!fullscreenCheckBox.isSelected()) {
                    ResolutionHeight.setText("<html><font color='white'>Resolution Height: " + HeightSlider.getValue()+"</font></html>");
                }else{
                    HeightSlider.setValue(screenSize.height);
                }
            }
        });
        fullscreenCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fullscreenCheckBox.isSelected()) {
                    ResolutionWidth.setText("<html><font color='white'>Resolution Width: " + screenSize.width+"</font></html>");
                    ResolutionHeight.setText("<html><font color='white'>Resolution Height: " + screenSize.height+"</font></html>");
                    WidthSlider.setValue(screenSize.width);
                    HeightSlider.setValue(screenSize.height);
                }
            }
        });
    }
}
