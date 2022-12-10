package Game;

import Pieces.Color;
import Pieces.Pawn;

/**
 * Armazena o tabuleiro e responsavel por posicionar as pecas.
 * @author Alefe de Lima
 * @author Jose Alisson
 * @author Matheus Yago
 * @author Weverton Vitor
 */
public class Game {

  private Table tabuleiro;

  public Game(boolean isTurn) {
    tabuleiro = new Table(isTurn);
    criarPecas();
  }
  
  /**
   * Posiciona peças no tabuleiro.
   * Utilizado na inicialização do jogo.
   */
  private void criarPecas() {
    this.criarPecasPretas();   
    this.criarPecasBrancas();

  }
  
  public void criarPecasPretas() {
    for(int i = 7; i > 4; i--) { // cria as 12 peças pretas
      for(int j = 0; j < 8; j += 2) {
        if (i % 2 == 1 && j == 0) {
          j++;
        }
        Square casa = tabuleiro.getSquare(j, i);
        new Pawn(casa, Color.BLACK);
      }
    }
  }
  
  public void criarPecasBrancas() {    
    for(int i = 0; i < 3; i++) { // cria as 12 peças brancas
      for(int j = 0; j < 8; j += 2) {
        if (i % 2 == 1 && j == 0) {
          j++;
        }
        Square casa = tabuleiro.getSquare(j, i);
        new Pawn(casa, Color.WHITE);
      }
    }
  }
  
  /**
   * Comanda uma Peça na posicao (origemX, origemY) fazer um movimento 
   * para (destinoX, destinoY).
   * 
   * @param origemX linha da Casa de origem.
   * @param origemY coluna da Casa de origem.
   * @param destinoX linha da Casa de destino.
   * @param destinoY coluna da Casa de destino.
   */
  public void moverPeca(int origemX, int origemY, int destinoX, int destinoY) {
    Square origem = tabuleiro.getSquare(origemX, origemY);
    Square destino = tabuleiro.getSquare(destinoX, destinoY);
    Pawn peca = (Pawn) origem.getPiece();
    peca.mover(destino, tabuleiro);
  }
  
  /**
   * @return o Tabuleiro em jogo.
   */
  public Table getTable() {
    return tabuleiro;
  }

  public void setTabuleiro(Table tabuleiro) {
    this.tabuleiro = tabuleiro;
  }
}
