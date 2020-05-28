package client;


import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * Main class for GUI
 */
public class ClientView extends Canvas{
    private static int WIDTH;
    private static int HEIGHT;
    private static boolean FULLSCREEN;

    /**
     * Reads configuration from .config file or creates one in case read failed.
     * @throws IOException
     */
    public static void ReadConfig() throws IOException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(Settings.configPath)));
            String[] line = bufferedReader.readLine().split(" ");
            WIDTH = Integer.parseInt(line[2]);
            line = bufferedReader.readLine().split(" ");
            HEIGHT = Integer.parseInt(line[2]);
            line = bufferedReader.readLine().split(" ");
            FULLSCREEN = Boolean.parseBoolean(line[2]);
        } catch (Exception e) {
            WIDTH = 1000;
            HEIGHT = 1000;
            FULLSCREEN = false;
            String string = "";
            string += "width = " + WIDTH+System.lineSeparator();
            string += "height = " + HEIGHT+System.lineSeparator();
            string += "fullscreen = " + FULLSCREEN+System.lineSeparator();
            FileWriter fileWriter = new FileWriter(Settings.configPath);
            fileWriter.write(string);
            fileWriter.flush();
            fileWriter.close();
        }
    }

    public static void main(String[] args) throws IOException {
        ReadConfig();
        JFrame frame = new JFrame("Splendor");
        if(FULLSCREEN){
            Dimension dimension = new Dimension(WIDTH, HEIGHT);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setUndecorated(true);
            frame.setPreferredSize(dimension);
            frame.setMinimumSize(dimension);
            frame.setMaximumSize(dimension);
        }else {
            Dimension dimension = new Dimension(WIDTH, HEIGHT);
            frame.setPreferredSize(dimension);
            frame.setMinimumSize(dimension);
            frame.setMaximumSize(dimension);
        }
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new Menu(frame).view);
        frame.pack();
        frame.setVisible(true);
    }
}
