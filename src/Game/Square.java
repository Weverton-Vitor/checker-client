package Game;

import Pieces.Color;
import Pieces.Piece;

import java.io.Serializable;

public class Square implements Serializable {

  private int x;
  private int y;
  private Piece peca;

  public Square(int x, int y) {
    this.x = x;
    this.y = y;
    this.peca = null;
  }

  /**
   * @param peca a Pe�a a ser posicionada nesta Casa.
   */
  public void putPiece(Piece peca) {
    this.peca = peca;
  }

  /**
   * Remove a peca posicionada nesta casa, se houver.
   */
  public void removerPiece(Table table) {
    // Decrementando a contagem de pecas
    if(peca.getColor() == Color.BLACK){
      table.setTotalBlack(table.getTotalBlack() - 1);
    } else {
      table.setTotalWhite(table.getTotalWhite() - 1);
    }
    peca = null;
  }

  public void removerPiece() {
    peca = null;
  }

  /**
   * @return a Piece posicionada nesta Casa, ou Null se a casa estiver livre.
   */
  public Piece getPiece() {
    return peca;
  }

  /**
   * @return retorna a posição x da casa
   */
  public int getXPosition() {
    return this.x;
  }

  /**
   * @return retorna a posição y da casa
   */
  public int getYPosition() {
    return this.y;
  }

  /**
   * @return true se existe uma pe�a nesta casa, caso contrario false.
   */
  public boolean hasPiece() {
    return peca != null;
  }

  public boolean estaNaDiagonalSuperiorDe(Square origin) {
    boolean topRightDiagonal = origin.getXPosition() + 1 == this.x && origin.getYPosition() + 1 == this.y;
    boolean topLeftDiagonal = origin.getXPosition() - 1 == this.x && origin.getYPosition() + 1 == this.y;

    return topRightDiagonal || topLeftDiagonal;
  }
}
