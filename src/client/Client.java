package client;

import Pieces.Piece;
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

  private ObjectOutputStream out;
  private ObjectInputStream in;
  private ExecutorService pool;

  public Client() throws IOException {
    this.socket = new Socket("127.0.0.1", 54323);
    this.out = new ObjectOutputStream(this.socket.getOutputStream());
    this.in = new ObjectInputStream(this.socket.getInputStream());
    this.pool = Executors.newCachedThreadPool();
  }

  public void connectSession(String code) throws ExecutionException, InterruptedException {
    ConnectSessionCallable task = new ConnectSessionCallable(out, in, code); // task callable que retorna o código da sala
    Future future = this.pool.submit(task);
    Message response = (Message) future.get(); // Obtendo a mensagem do servidor
    setCodeSession(response.getCodeSession());
    setColor(response.getColor());
  }

  public void createSession() throws ExecutionException, InterruptedException {
    CreateSessionCallable task = new CreateSessionCallable(out, in); // task callable que retorna o código da sala
    Future future = this.pool.submit(task);
    Message response = (Message) future.get();
    // Obtendo a mensagem do servidor
    setCodeSession(response.getCodeSession());
    setColor(response.getColor());

  }

  public void exitSession() {

  }

  public void move(int origemX, int origemY, int destinoX, int destinoY) throws ExecutionException, InterruptedException {
    MovePieceCallable task = new MovePieceCallable(out, in, this.codeSession, origemX, origemY, destinoX, destinoX); // task callable que retorna o código da sala
    Future future = this.pool.submit(task);
    Message response = (Message) future.get(); // Obtendo a mensagem do servidor
    System.out.println(response.getTable());
  }



//  public Client() throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
//    // Ao criar um client/player uma sala é criada automaticamente junto com sua conexão com o servidor
//    ExecutorService executor = Executors.newCachedThreadPool();
//    CreateSessionCallable task = new CreateSessionCallable(this); // task callable que retorna o código da sala
//    Future future = executor.submit(task);
//    Message response = (Message) future.get(); // Obtendo a mensagem do servidor
//    setCodeSession(response.getCodeSession());
//
////    task.start();
//  }

//  public Client(String codeSession) throws ExecutionException, InterruptedException {
//    this.codeSession = codeSession;
//
//    ExecutorService executor = Executors.newCachedThreadPool();
//    ConnectSessionCallable task = new ConnectSessionCallable(this, this.codeSession); // task callable que retorna o código da sala
//    Future future = executor.submit(task);
//    Message response = (Message) future.get(); // Obtendo a mensagem do servidor
////    setCodeSession(response.getCodeSession());
//    System.out.println(response.getAction());
//  }

  public String getCodeSession() {
    return codeSession;
  }

  public void setCodeSession(String codeSession) {
    this.codeSession = codeSession;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }
}
