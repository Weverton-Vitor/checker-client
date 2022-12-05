package Pieces;

import Game.Square;
import Game.Table;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Piece implements Serializable {
  protected Square casa;
  protected Square casaDeCaptura;
  protected int moves;
  protected Color color;

  abstract boolean couldPromote();
  abstract void promote();
  abstract public boolean isAllowedPosition(Square destino, Table tabuleiro);

  // # Substituir tipo por cor
  public Piece(Square casa, Color cor) {
    this.casa = casa;
    this.color = cor;
    this.moves = 0;
    casa.putPiece(this);
  }
  /**
   * Movimenta a peca para uma nova casa.
   * @param destino nova casa que ira conter esta peca.
   */
  public void mover(Square destino, Table tabuleiro) {
    if(this.couldMove(destino, tabuleiro)) {
      if (this.podeCapturar(destino, tabuleiro)) {
        this.capturar(destino, tabuleiro);
      }

      this.casa.removerPiece();
      destino.putPiece(this);
      this.casa = destino;
      this.moves += 1;


      if (this.color == Color.BLACK) {
        System.out.println("Peça vermelha foi movida");
      } else {
        System.out.println("Peça branca foi movida");
      }

      if (this.couldPromote()) {
        this.promote();
      }

      // Verificando se a peça pode se mover novamente
      if (tabuleiro.getStoledPiece() == this) {
        boolean aindaPodeCapturar = this.couldStole(tabuleiro);

        // Depois que a peça se move é analiádo se existe alguma possibilidade de captura
        if (!aindaPodeCapturar) {
          tabuleiro.removeStoledPiece();
          tabuleiro.toggleTurno();
        }

        if ((this.color == Color.BLACK) && aindaPodeCapturar) {
          System.out.println("Peça branca pode se mover novamente");
        } else if(aindaPodeCapturar) {
          System.out.println("Peça branca pode se mover novamente");
        }
      } else {
        tabuleiro.toggleTurno();
      }

      // Só troca o turno se a peça não tiver capturado outra

    }
  }

  /**
   * @return a cor da peca.
   */
  public Color getColor() {
    return this.color;
  }

  public boolean ehBranca() {
    return this.color == Color.WHITE;
  }

  public boolean ehMesmaColor(Piece peca) {
    return this.color == peca.getColor();
  }

  public int getDeltaDeCaptura() {
    return 2;
  }

  public boolean equals(Piece peca) {
    int x = this.casa.getXPosition();
    int y = this.casa.getYPosition();
    int pecaX = peca.casa.getXPosition();
    int pecaY = peca.casa.getYPosition();

    return x == pecaX && y == pecaY;
  }

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

    if (moveAndCapture) {
      if(Math.abs(deltaX) < this.getDeltaDeCaptura() && Math.abs(deltaY) < this.getDeltaDeCaptura()) {
        if (pecaIsBlack && table.isBlackRound()) {
          System.out.println("A peça vermelha é obrigada a captura");
        } else if (!pecaIsBlack && !table.isBlackRound()){
          System.out.println("A peça branca é obrigada a captura");
        }
        return false;
      }
    }

    // Só move a peça se o turno for o correto para a cor da peça
    if ((this.color != table.corTurno())) {
      System.out.println("Turno errado");
      return false;
    }

    ArrayList<Piece> vulnerableWhitePieces = table.findVulnerablePieces(this.ehBranca());
    System.out.println(vulnerableWhitePieces.toString());

    boolean hasVulnerablePieces = vulnerableWhitePieces.size() > 0;
    boolean couldStole = vulnerableWhitePieces.contains(this);

    if ((hasVulnerablePieces && !couldStole) || ( couldStole && Math.abs(deltaX) != 2)) {
      return false;
    }

    boolean capture = podeCapturar(destiny, table);
    return !destiny.hasPiece() && (capture || this.isAllowedPosition(destiny, table));
  }

  /**
   * Executa em 2 modos, pesquisa e não pesquisa
   * Em modo pesquisa apenas verifica se há alguma peça para capturar sem remove-la.
   * Em modo não pesquisa além de verificar se há uma peça para capturar ainda remove essa peça
   *
   * @param destiny A casa para onde a peça vai depois que fazer a captura.
   * @param table Tabuleiro do jogo.
   *
   */
  public void capturar(Square destiny, Table table) {
    this.casaDeCaptura.removerPiece(table);
    table.setStoledPiece(this);
  }

  public boolean couldStole(Table table) {
    ArrayList<Integer> notAllowedPositions = new ArrayList<>();
    notAllowedPositions.add(-1);
    notAllowedPositions.add(8);

    for (int i = -1; i <= 1; i += 2) {
      for (int j = -1; j <= 1; j += 2) {
        int x = this.casa.getXPosition() + i;
        int y = this.casa.getYPosition() + j;

        if (notAllowedPositions.contains(x) || notAllowedPositions.contains(y)) {
          continue;
        }

        Square casa = table.getSquare(x, y);
        Piece piece = casa.getPiece();
        int stoleX = x + i;
        int stoleY = y + j;

        if (notAllowedPositions.contains(stoleX) || notAllowedPositions.contains(stoleY)) {
          continue;
        }

        Square stoleSquare = table.getSquare(stoleX, stoleY);
        boolean stoleSquareFree = !stoleSquare.hasPiece();

        if (casa.hasPiece() && !this.ehMesmaColor(piece) && stoleSquareFree) {
          return true;
        }
      }
    }

    return false;
  }


  public boolean podeCapturar(Square destino, Table tabuleiro) {
    int posicaoXOrigem = this.casa.getXPosition();
    int posicaoYOrigem = this.casa.getYPosition();
    int posicaoXDestino = destino.getXPosition();
    int posicaoYDestino = destino.getYPosition();

    int deltaX = posicaoXDestino - posicaoXOrigem;
    int deltaY = posicaoYDestino - posicaoYOrigem;
    if(Math.abs(deltaX) == Math.abs(deltaY) && Math.abs(deltaX) == 2) {
      // Determinando a possição intermediria da pessa que será capturada
      int xCapture = deltaX < 0 ? posicaoXDestino + 1 :  posicaoXDestino - 1;
      int yCapture = deltaY < 0 ? posicaoYDestino + 1 :  posicaoYDestino - 1;

      // Só faz a captura e o movimento se houver um peça na casa intermetiaria do movimento de 2 casas
      Square casaDeCaptura = tabuleiro.getSquare(xCapture, yCapture);
      Piece pecaDeCaptura = casaDeCaptura.getPiece();

      if (casaDeCaptura.hasPiece() && !this.ehMesmaColor(pecaDeCaptura)) {
        this.casaDeCaptura = casaDeCaptura;
        return true;
      }
    }

    //Não move
    return false;

  }
}
