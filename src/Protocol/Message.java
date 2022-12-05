package Protocol;

import java.io.Serializable;

public class Message implements Serializable {
    private String action;
    private String color;
    private String codeSession;

    private int origemX;
    private int origemY;
    private int destinoX;
    private int destinoY;


    public Message() {
    }

    public Message(int origemX, int origemY, int destinoX, int destinoY) {
        this.origemX = origemX;
        this.origemY = origemY;
        this.destinoX = destinoX;
        this.destinoY = destinoY;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCodeSession() {
        return codeSession;
    }

    public void setCodeSession(String codeSession) {
        this.codeSession = codeSession;
    }

    public int getOrigemX() {
        return origemX;
    }

    public void setOrigemX(int origemX) {
        this.origemX = origemX;
    }

    public int getOrigemY() {
        return origemY;
    }

    public void setOrigemY(int origemY) {
        this.origemY = origemY;
    }

    public int getDestinoX() {
        return destinoX;
    }

    public void setDestinoX(int destinoX) {
        this.destinoX = destinoX;
    }

    public int getDestinoY() {
        return destinoY;
    }

    public void setDestinoY(int destinoY) {
        this.destinoY = destinoY;
    }
}
