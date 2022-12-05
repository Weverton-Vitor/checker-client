package Pieces;

import Game.Square;
import Game.Table;

import java.io.Serializable;

public class Pawn extends Piece implements Serializable {
  public int deltaMovement;
  public int deltaStole; // ATENÇÃO WEVERTON
  public int promoteOnY; // ATENÇÃO WEVERTON

  public Pawn(Square casa, Color cor) {
    super(casa, cor);
    this.deltaStole = 2;
    if (cor == Color.WHITE) {
      this.deltaMovement = 1;
      this.promoteOnY = 7;
    } else {
      this.promoteOnY = 0;
      this.deltaMovement = -1;
    }
  }

  public boolean isAllowedPosition(Square destiny, Table table) {
    int positionXOrigin = this.casa.getXPosition();
    int positionYOrigin = this.casa.getYPosition();
    int positionXDestiny = destiny.getXPosition();
    int positionYDestiny = destiny.getYPosition();

    int deltaX = positionXDestiny - positionXOrigin;
    int deltaY = positionYDestiny - positionYOrigin;


    return Math.abs(deltaX) == Math.abs(deltaY) && deltaY == deltaMovement;
  }

  public boolean couldPromote() {
    return this.casa.getYPosition() == promoteOnY;
  }

  @Override
  void promote() {
    this.casa.removerPiece();
    Piece dame = new Dame(casa, this.color);
    this.casa.putPiece(dame);
  }
}
