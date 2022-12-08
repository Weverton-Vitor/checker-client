package client;

        import Protocol.Message;

        import java.io.IOException;
        import java.io.ObjectInputStream;
        import java.io.ObjectOutputStream;
        import java.util.concurrent.Callable;

public class WaitConnectionCallable implements Callable {
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public WaitConnectionCallable(ObjectOutputStream out, ObjectInputStream in) {
        this.out = out;
        this.in = in;
    }


    @Override
    public Object call() throws Exception {
        try {
            // Criando obejto de mensagem com a ação de criar uma sala
            Message response = (Message) in.readObject();
            System.out.println(response.getAction());
            return response;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
