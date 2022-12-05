package client;

import Protocol.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

public class CreateSessionCallable implements Callable {

    private ObjectOutputStream out;
    private ObjectInputStream in;

    public CreateSessionCallable(ObjectOutputStream out, ObjectInputStream in) {
        this.out = out;
        this.in = in;
    }


    @Override
    public Object call() throws Exception {
        try {
            // Criando obejto de mensagem com a ação de criar uma sala
            Message msgConnection = new Message();
            msgConnection.setAction("CREATE_SESSION");
            out.writeObject(msgConnection);
            Message response = (Message) in.readObject();
//            s.close();
            return response;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
