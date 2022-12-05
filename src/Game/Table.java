package Game;

import Pieces.Color;
import Pieces.Dame;
import Pieces.Piece;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public class Table implements Serializable {
  private Piece[][] table;
  private Square[][] squares;
  private Hashtable<Integer, Piece> pieces = new Hashtable<>();
  private boolean blackRound;
  private int totalWhite = 12;
  private int totalBlack = 12;
  private Piece stoledPiece;

  public Piece getStoledPiece() {
    return stoledPiece;
  }

  public Piece getPieceById(int id) {
    return this.pieces.get(id);
  }

  public void setStoledPiece(Piece stoledPiece) {
    this.stoledPiece = stoledPiece;
  }

  public void removeStoledPiece() {
    this.stoledPiece = null;
  }

  public Table() {
    squares = new Square[8][8];
    for (int x = 0; x < 8; x++) {
      for (int y = 0; y < 8; y++) {
        Square square = new Square(x, y);
        squares[x][y] = square;
      }
    }
  }
  /**
   * @param x linha
   * @param y coluna
   * @return Square na posicao (x,y)
   */
  public Square getSquare(int x, int y) {
    return squares[x][y];
  }

  public boolean isBlackRound() {
    return blackRound;
  }

  public Color corTurno() {
    return blackRound ? Color.BLACK : Color.WHITE;
  }

  public void setBlackRound(boolean blackRound) {
    this.blackRound = blackRound;
  }

  public void toggleTurno() {
    blackRound = !blackRound;
  }

  public ArrayList<Piece> findVulnerablePieces(boolean white) {
    if (white) {
      return this.findVulnerableWhitePieces();
    }
    return this.findVulnerableBlackPieces();
  }

  public ArrayList<Piece> findVulnerableWhitePieces() {
    ArrayList<Piece> vulnerablePieces = new ArrayList<Piece>();

    for(int i = 0; i < 8; i++) {
      for(int j = 0; j < 8; j++) {
        Square casa = squares[i][j];
        Piece peca = casa.getPiece();
        if(casa.hasPiece() && peca.ehBranca() && peca.couldStole(this)) {
          vulnerablePieces.add(peca);
        }
      }
    }

    return vulnerablePieces;
  }

  public ArrayList<Piece> findVulnerableBlackPieces() {
    ArrayList<Piece> pecasQPodemCapturar = new ArrayList<Piece>();

    for(int i = 0; i < 8; i++) {
      for(int j = 0; j < 8; j++) {
        Square square = squares[i][j];
        Piece piece = square.getPiece();
        if(square.hasPiece() && !piece.ehBranca() && piece.couldStole(this)) {
          System.out.println("É dama? vv");
          System.out.println(piece instanceof Dame);
          pecasQPodemCapturar.add(piece);
        }
      }
    }

    return pecasQPodemCapturar;
  }

  public int getTotalWhite() {
    return totalWhite;
  }

  public void setTotalWhite(int totalWhite) {
    this.totalWhite = totalWhite;
  }

  public int getTotalBlack() {
    return totalBlack;
  }

  public void setTotalBlack(int totalBlack) {
    this.totalBlack = totalBlack;
  }
}
