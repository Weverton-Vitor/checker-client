package client;

import Protocol.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

public class WaitMoveCallable implements Callable {
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String codeSession;
    private String color;


    public WaitMoveCallable(ObjectOutputStream out, ObjectInputStream in, String codeSession, String color) {
        this.out = out;
        this.in = in;
        this.codeSession = codeSession;
        this.color = color;
    }


    @Override
    public Object call() throws Exception {
        try {

            // Abrindo o stream de escuta
            Message response = (Message) in.readObject();
            return response;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
