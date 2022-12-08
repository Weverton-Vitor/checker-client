package client;

import Game.Game;
import Game.Table;
import Game.Square;
import Pieces.Piece;
import Protocol.Message;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;

public class Client {
  private String color;
  private String codeSession;

  private Semaphore waitToPlay;

  private Socket socket;

  private ObjectOutputStream out;
  private ObjectInputStream in;
  private ExecutorService pool;

  public Client() throws IOException {
    this.socket = new Socket("127.0.0.1", 8888);
    this.out = new ObjectOutputStream(this.socket.getOutputStream());
    this.in = new ObjectInputStream(this.socket.getInputStream());
    this.pool = Executors.newCachedThreadPool();
    this.waitToPlay = new Semaphore(1, true);
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

  public Table move(int origemX, int origemY, int destinoX, int destinoY, Table oldTable) throws ExecutionException, InterruptedException {
//    this.waitToPlay.acquire();
    System.out.println("Cor: " + this.color);
    MovePieceCallable task = new MovePieceCallable(out, in, this.codeSession, origemX, origemY, destinoX, destinoY, this.color); // task callable que retorna o código da sala
    Future future = this.pool.submit(task);
    Message response = (Message) future.get();
//    System.out.println(response.getAction());
    if (response.getAction().equals("WRONG_TURN")) { // Em caso de turno errado o tabuleiro sem alterações é retornado
      System.out.println("turno errado");
      return oldTable;
    }

    

    System.out.println("Turno certo");
    return response.getTable();
  }

  public Table waitMove() throws ExecutionException, InterruptedException {
    WaitMoveCallable waitTask = new WaitMoveCallable(this.getOut(), this.getIn(), this.codeSession, this.color);
    Future future = this.pool.submit(waitTask);
    Message response = (Message) future.get();
    System.out.println(response.getTable());
    return  response.getTable();
  }

  public void waitConnection() throws ExecutionException, InterruptedException {
    WaitConnectionCallable waitTask = new WaitConnectionCallable(this.getOut(), this.getIn());
    Future future = this.pool.submit(waitTask);
    Message response = (Message) future.get();
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

  public ObjectOutputStream getOut() {
    return out;
  }

  public ObjectInputStream getIn() {
    return in;
  }

  public ExecutorService getPool() {
    return pool;
  }

  public Semaphore getWaitToPlay() {
    return waitToPlay;
  }

  public void setWaitToPlay(Semaphore waitToPlay) {
    this.waitToPlay = waitToPlay;
  }


}


