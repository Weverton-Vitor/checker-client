package client;

        import Protocol.Message;

        import java.io.IOException;
        import java.io.ObjectInputStream;
        import java.io.ObjectOutputStream;
        import java.net.Socket;
        import java.util.concurrent.Callable;

public class ConnectSessionCallable implements Callable {
    private Client player;
    private String codeSession;

    public ConnectSessionCallable(Client player, String codeSession) {
        this.player = player;
        this.codeSession = codeSession;
    }


    @Override
    public Object call() throws Exception {
        try {
            // Conecatando ao servidor
            Socket s = new Socket("127.0.0.1", 54323);
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());

            // Criando obejto de mensagem com a ação de criar uma sala
            Message msgConnection = new Message();
            msgConnection.setAction("CONNECT_SESSION");
            msgConnection.setCodeSession(this.codeSession);
            out.writeObject(msgConnection);
            Message response = (Message) in.readObject();
            this.player.setSocket(s); // passando o socket para o objeto do player
//            s.close();
            return response;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public Client getPlayer() {
        return player;
    }

    public void setPlayer(Client player) {
        this.player = player;
    }
}
