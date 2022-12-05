package Pieces;

import Game.Square;
import Game.Table;

import java.io.Serializable;
import java.util.ArrayList;

public class Dame extends Piece implements Serializable {
  public Dame(Square casa, Color cor) {
    super(casa, cor);
  }

  @Override
  public boolean isAllowedPosition(Square destino, Table tabuleiro) {
    int posicaoXOrigem = this.casa.getXPosition();
    int posicaoYOrigem = this.casa.getYPosition();
    int posicaoXDestino = destino.getXPosition();
    int posicaoYDestino = destino.getYPosition();

    int deltaX = posicaoXDestino - posicaoXOrigem;
    int deltaY = posicaoYDestino - posicaoYOrigem;

    if (Math.abs(deltaX) != Math.abs(deltaY)) {
      return false;
    }

    for (int i = 1; i <= Math.abs(deltaX); i++) {
      int posicaoXverificar = deltaX > 0 ? posicaoXOrigem + i : posicaoXOrigem - i;
      int posicaoYverificar = deltaY > 0 ? posicaoYOrigem + i : posicaoYOrigem - i;

      Square casa = tabuleiro.getSquare(posicaoXverificar, posicaoYverificar);
      if (casa.hasPiece()) return false;
    }

    return true;
  }

  @Override
  boolean couldPromote() {
    return false;
  }

  @Override
  void promote() {}

  @Override
  public boolean podeCapturar(Square destino, Table tabuleiro) {
    int posicaoXOrigem = this.casa.getXPosition();
    int posicaoYOrigem = this.casa.getYPosition();
    int posicaoXDestino = destino.getXPosition();
    int posicaoYDestino = destino.getYPosition();

    int deltaX = posicaoXDestino - posicaoXOrigem;
    int deltaY = posicaoYDestino - posicaoYOrigem;

    if (Math.abs(deltaX) != Math.abs(deltaY)) {
      return false;
    }

    if (Math.abs(deltaX) == 1) return false;

    int quantidadeDePieces = 0;
    Piece ultimaPiece = null;
    Square casaDaUltimaPiece = null;

    for (int i = 1; i <= Math.abs(deltaX); i++) {
      int posicaoXverificar = deltaX > 0 ? posicaoXOrigem + i : posicaoXOrigem - i;
      int posicaoYverificar = deltaY > 0 ? posicaoYOrigem + i : posicaoYOrigem - i;

      Square casa = tabuleiro.getSquare(posicaoXverificar, posicaoYverificar);
      if (casa.hasPiece()) {
        ultimaPiece = casa.getPiece();
        casaDaUltimaPiece = casa;
        quantidadeDePieces++;
      }
    }

    if (quantidadeDePieces == 1 && !this.ehMesmaColor(ultimaPiece)) {
      this.casaDeCaptura = casaDaUltimaPiece;
      return true;
    }

    return false;
  }

  @Override
  public boolean couldStole(Table table) {
    for (int i = -1; i <= 1; i += 2) {
      for (int j = -1; j <= 1; j += 2) {
        for (int k = 1; k < 8; k++) {
          int x = this.casa.getXPosition() + k * i;
          int y = this.casa.getYPosition() + k * j;
          boolean posicaoInvalida = x < 0 || x > 7 || y < 0 || y > 7;
          if (posicaoInvalida) continue;

          Square casa = table.getSquare(x, y);
          Piece peca = casa.getPiece();
          int xDeCaptura = x + i;
          int yDeCaptura = y + j;
          posicaoInvalida = xDeCaptura < 0 || xDeCaptura > 7 || yDeCaptura < 0 || yDeCaptura > 7;
          if (posicaoInvalida) {
            continue;
          }

          Square casaDeCaptura = table.getSquare(xDeCaptura, yDeCaptura);
          boolean casaDeCapturaLivre = !casaDeCaptura.hasPiece();

          if (casa.hasPiece() && !this.ehMesmaColor(peca) && casaDeCapturaLivre) {
            return true;
          }
          if (casa.hasPiece() && !this.ehMesmaColor(peca) && !casaDeCapturaLivre) k = 8;
        }
      }
    }

    return false;
  }

  public ArrayList<int[]> encontrarSquaresParaCapturar(Table tabuleiro) {
    ArrayList<int[]> casas = new ArrayList<int[]>();

    for (int i = -1; i <= 1; i += 2) {
      for (int j = -1; j <= 1; j += 2) {
        for (int k = 1; k < 8; k++) {
          int x = this.casa.getXPosition() + k * i;
          int y = this.casa.getYPosition() + k * j;
          boolean posicaoInvalida = x < 0 || x > 7 || y < 0 || y > 7;
          if (posicaoInvalida) continue;

          Square casa = tabuleiro.getSquare(x, y);
          Piece peca = casa.getPiece();
          int xDeCaptura = x + i;
          int yDeCaptura = y + j;
          posicaoInvalida = xDeCaptura < 0 || xDeCaptura > 7 || yDeCaptura < 0 || yDeCaptura > 7;
          if (posicaoInvalida) {
            continue;
          }

          Square casaDeCaptura = tabuleiro.getSquare(xDeCaptura, yDeCaptura);
          boolean casaDeCapturaLivre = !casaDeCaptura.hasPiece();

          if (casa.hasPiece() && !this.ehMesmaColor(peca) && casaDeCapturaLivre) {
            int[] deltas = {k*i+i, k*j+j};
            casas.add(deltas);
          }
        }
      }
    }

    return casas;
  }

  private boolean mesmaDirecao(int delta1, int delta2){
    int direcao1 = delta1 / Math.abs(delta1);
    int direcao2 = delta2 / Math.abs(delta2);

    return direcao1 == direcao2;
  }

  @Override
  public boolean couldMove(Square destiny, Table table) {
    boolean pecaIsBlack = !this.ehBranca();
    int posicaoXOrigem = this.casa.getXPosition();
    int posicaoYOrigem = this.casa.getYPosition();
    int posicaoXDestino = destiny.getXPosition();
    int posicaoYDestino = destiny.getYPosition();

    int deltaX = posicaoXDestino - posicaoXOrigem;
    int deltaY = posicaoYDestino - posicaoYOrigem;

    boolean moveAndCapture = table.getStoledPiece() == this;

    if (Math.abs(deltaX) != Math.abs(deltaY)) return false;
    // Verificando se outra peça fez um movimento de captura e tem que se mover de novo
    if (!moveAndCapture) {
      if (pecaIsBlack && table.isBlackRound()) {
        System.out.println("Apenas a peça vermelha que fez a captura pode se mover");
      } else if (!pecaIsBlack && !table.isBlackRound()){
        System.out.println("Apenas a peça branca que fez a captura pode se mover");
      }
    }

    if (moveAndCapture && !this.vaiCapturar(table, deltaX, deltaY)) {
      return false;
    }

    // Só move a peça se o turno for o correto para a cor da peça
    if (this.color != table.corTurno()) {
      System.out.println("Turno errado");
      System.out.println(this.color);
      return false;
    }

    ArrayList<Piece> whiteVulnerablePieces = table.findVulnerablePieces(this.ehBranca());
    System.out.println(whiteVulnerablePieces.toString());

    boolean hasVulnerablePieces = whiteVulnerablePieces.size() > 0;
    boolean couldStole = whiteVulnerablePieces.contains(this);

    if (hasVulnerablePieces && !couldStole) {
      return false;
    } else if (couldStole && !this.vaiCapturar(table, deltaX, deltaY)) {
      return false;
    }

    //  Se a peça for se mover e capturar outra peça a flag de movimento e captura é setada True(para realizar outro movimento)
    boolean capture = podeCapturar(destiny, table);
    boolean move = !destiny.hasPiece() && (capture || this.isAllowedPosition(destiny, table));

    return move;
  }

  public boolean vaiCapturar(Table tabuleiro, int deltaX, int deltaY) {
    ArrayList<int[]> deltas = this.encontrarSquaresParaCapturar(tabuleiro);
    boolean vaiCapturar = false;
    for (int[] delta : deltas) {
      boolean deltaXvalido = Math.abs(deltaX) >= Math.abs(delta[0]) && this.mesmaDirecao(deltaX, delta[0]);
      boolean deltaYvalido = Math.abs(deltaY) >= Math.abs(delta[1]) && this.mesmaDirecao(deltaY, delta[1]);
      if (deltaXvalido && deltaYvalido) {
        vaiCapturar = true;
      }
    }

    return vaiCapturar;
  }
}
