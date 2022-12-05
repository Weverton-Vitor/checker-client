package client;

        import Protocol.Message;

        import java.io.IOException;
        import java.io.ObjectInputStream;
        import java.io.ObjectOutputStream;
        import java.net.Socket;
        import java.util.concurrent.Callable;

public class ConnectSessionCallable implements Callable {
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String codeSession;

    public ConnectSessionCallable(ObjectOutputStream out, ObjectInputStream in, String codeSession) {
        this.out = out;
        this.in = in;
        this.codeSession = codeSession;
    }


    @Override
    public Object call() throws Exception {
        try {
            // Criando obejto de mensagem com a ação de criar uma sala
            Message msgConnection = new Message();
            msgConnection.setAction("CONNECT_SESSION");
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
