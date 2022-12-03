package client;

import Protocol.Message;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Client {
  private String color;
  private String codeSession;

  private Socket socket;

  public Client() throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
    // Ao criar um client/player uma sala é criada automaticamente junto com sua conexão com o servidor
    ExecutorService executor = Executors.newCachedThreadPool();
    CreateSessionCallable task = new CreateSessionCallable(this); // task callable que retorna o código da sala
    Future future = executor.submit(task);
    Message response = (Message) future.get(); // Obtendo a mensagem do servidor
    setCodeSession(response.getCodeSession());

//    task.start();
  }

  public Client(String codeSession) throws ExecutionException, InterruptedException {
    this.codeSession = codeSession;

    ExecutorService executor = Executors.newCachedThreadPool();
    ConnectSessionCallable task = new ConnectSessionCallable(this, this.codeSession); // task callable que retorna o código da sala
    Future future = executor.submit(task);
    Message response = (Message) future.get(); // Obtendo a mensagem do servidor
//    setCodeSession(response.getCodeSession());
    System.out.println(response.getAction());
  }

  public String getCodeSession() {
    return codeSession;
  }

  public void setCodeSession(String codeSession) {
    this.codeSession = codeSession;
  }

  public Socket getSocket() {
    return socket;
  }

  public void setSocket(Socket socket) {
    this.socket = socket;
  }
}
