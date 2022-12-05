package client;

import Protocol.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

public class MovePieceCallable implements Callable {

    private Socket playerSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String codeSession;

    private int origemX;
    private int origemY;
    private int destinoX;
    private int destinoY;


    public MovePieceCallable(ObjectOutputStream out, ObjectInputStream in, String codeSession, int origemX, int origemY, int destinoX, int destinoY) {
        this.out = out;
        this.in = in;
        this.codeSession = codeSession;
        this.origemX = origemX;
        this.origemY = origemY;
        this.destinoX = destinoX;
        this.destinoY = destinoY;
    }


    @Override
    public Object call() throws Exception {
        try {
            System.out.println("Movendo");

            // Conecatando ao servidor
//            ObjectOutputStream out = new ObjectOutputStream(playerSocket.getOutputStream());
//            ObjectInputStream in = new ObjectInputStream(playerSocket.getInputStream());
            // Criando obejto de mensagem com a ação de criar uma sala
            Message msgConnection = new Message(origemX, origemY, destinoX, destinoX);
            msgConnection.setAction("MOVE");
            msgConnection.setCodeSession(this.codeSession);
            System.out.println(this.codeSession);
            out.writeObject(msgConnection);
            Message response = (Message) in.readObject();
            return response;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}

