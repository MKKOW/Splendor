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

    public Settings(JFrame frame) {
        Toolkit tk =Toolkit.getDefaultToolkit();
        Dimension screenSize=tk.getScreenSize();
        ResolutionWidth.setText("<html><font color='white'>Resolutin Width: " + frame.getWidth()+"</font></html>");
        ResolutionHeight.setText("<html><font color='white'>Resolutin Width: " + frame.getHeight()+"</font></html>");
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
                    ResolutionWidth.setText("<html><font color='white'>Resolutin Width: " + WidthSlider.getValue()+"</font></html>");
                }else{
                    WidthSlider.setValue(screenSize.width);
                }
            }
        });
        HeightSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(!fullscreenCheckBox.isSelected()) {
                    ResolutionHeight.setText("<html><font color='white'>Resolutin Height: " + HeightSlider.getValue()+"</font></html>");
                }else{
                    HeightSlider.setValue(screenSize.height);
                }
            }
        });
        fullscreenCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fullscreenCheckBox.isSelected()) {
                    ResolutionWidth.setText("<html><font color='white'>Resolutin Width: " + screenSize.width+"</font></html>");
                    ResolutionHeight.setText("<html><font color='white'>Resolutin Height: " + screenSize.height+"</font></html>");
                    WidthSlider.setValue(screenSize.width);
                    HeightSlider.setValue(screenSize.height);
                }
            }
        });
    }
}
