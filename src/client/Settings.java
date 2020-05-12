package client;

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
    private JButton applyButton;
    protected JPanel view;
    private JButton returnButton;
    private JSlider HeightSlider;
    private JLabel ResolutionHeight;
    private JSlider WidthSlider;
    private JLabel ResolutionWidth;
    private JCheckBox fullscreenCheckBox;
    private JLabel fullscreenBox;

    public Settings(JFrame frame) {
        Toolkit tk =Toolkit.getDefaultToolkit();
        Dimension screenSize=tk.getScreenSize();
        ResolutionWidth.setText("Resolutin Width: "+frame.getWidth());
        ResolutionHeight.setText("Resolution Heigth: "+frame.getHeight());
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
                    fileWriter = new FileWriter(".config");
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
                    ResolutionWidth.setText("Resolution Width: " + WidthSlider.getValue());
                }else{
                    WidthSlider.setValue(screenSize.width);
                }
            }
        });
        HeightSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(!fullscreenCheckBox.isSelected()) {
                    ResolutionHeight.setText("Resolution Height: " + HeightSlider.getValue());
                }else{
                    HeightSlider.setValue(screenSize.height);
                }
            }
        });
        fullscreenCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fullscreenCheckBox.isSelected()) {
                    ResolutionWidth.setText("Resolutin Width: " + screenSize.width);
                    ResolutionHeight.setText("Resolution Heigth: " + screenSize.height);
                    WidthSlider.setValue(screenSize.width);
                    HeightSlider.setValue(screenSize.height);
                }
            }
        });
    }
}
