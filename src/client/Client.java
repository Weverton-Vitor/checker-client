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

  private Socket socket;

  private ObjectOutputStream out;
  private ObjectInputStream in;
  private ExecutorService pool;

  public Client() throws IOException {
    // Criando a conexão
    this.socket = new Socket("127.0.0.1", 8888);

    // Obtendo os streams de dados
    this.out = new ObjectOutputStream(this.socket.getOutputStream());
    this.in = new ObjectInputStream(this.socket.getInputStream());

    // Cada cliente tem um pool de threads
    this.pool = Executors.newCachedThreadPool();
  }

  // Conectar a uma sala
  public boolean connectSession(String code) throws ExecutionException, InterruptedException {
    // task callable que retorna o código da sala
    ConnectSessionCallable task = new ConnectSessionCallable(out, in, code);
    Future future = this.pool.submit(task);
    Message response = (Message) future.get(); // Obtendo a mensagem do servidor

    // Sala inexistente
    if (response.getAction().equals("NOT_FOUND_SESSION")){
      return false;
    }

    // Se a sala existir o código da sala é retornado
    setCodeSession(response.getCodeSession());
    setColor(response.getColor());
    return true;
  }

  // Criar uma sala
  public void createSession() throws ExecutionException, InterruptedException {
    // task callable que retorna o código da sala
    CreateSessionCallable task = new CreateSessionCallable(out, in);
    Future future = this.pool.submit(task);
    Message response = (Message) future.get();
    // Obtendo a mensagem do servidor
    setCodeSession(response.getCodeSession());
    setColor(response.getColor());

  }

  // Envia a ção de movimento para o servidor
  public Table move(int origemX, int origemY, int destinoX, int destinoY, Table oldTable) throws ExecutionException, InterruptedException {
    System.out.println("Cor: " + this.color);
    // task callable que retorna o novo tabuleiro
    MovePieceCallable task = new MovePieceCallable(out, in, this.codeSession, origemX, origemY, destinoX, destinoY, this.color);
    Future future = this.pool.submit(task);
    Message response = (Message) future.get();

    // Em caso de turno errado o tabuleiro sem alterações é retornado
    if (response.getAction().contains("WRONG")) {
      System.out.println("turno errado");
      return oldTable;
    }

    // Retorna o novo tabuleiro
    System.out.println("Turno certo");
    return response.getTable();
  }

  // Espera pelo movimento do outro jogador
  public Table waitMove() throws ExecutionException, InterruptedException {
    WaitMoveCallable waitTask = new WaitMoveCallable(this.getOut(), this.getIn(), this.codeSession, this.color);
    Future future = this.pool.submit(waitTask);
    Message response = (Message) future.get();
    System.out.println(response.getTable());
    return  response.getTable();
  }

  // Espera outro jogador se conectar a sala
  public void waitConnection() throws ExecutionException, InterruptedException {
    WaitConnectionCallable waitTask = new WaitConnectionCallable(this.getOut(), this.getIn());
    Future future = this.pool.submit(waitTask);
    Message response = (Message) future.get();
    System.out.println(response.getTable());
  }


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


}


