package server;

import java.io.Serializable;

public class Card implements Serializable {
    int blue = 0;
    int red = 0;
    int green = 0;
    String name = "";
    public Card(int blue, int red, int green, String name) {
        this.blue = blue;
        this.red = red;
        this.green = green;
        this.name = name;
    }

    public Card() {

    }

    @Override
    public String toString() {
        return "Card{" +
                "blue=" + blue +
                ", red=" + red +
                ", green=" + green +
                ", name='" + name + '\'' +
                '}';
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

