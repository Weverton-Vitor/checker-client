package Screens;

import Game.Game;
import Game.Square;
import Game.Table;
import Pieces.Dame;
import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Cor;

import javax.swing.*;
import java.awt.*;

/**
 * Interface Grafica do Tabuleiro do jogo.
 * 
 * @author Alan Moraes &lt;alan@ci.ufpb.br&gt;
 * @author Leonardo Villeth &lt;lvilleth@cc.ci.ufpb.br&gt;
 */
public class TableGUI extends JPanel {

  private JanelaPrincipal janela;
  private SquareGUI[][] casas;

  /**
   * Creates new form Tabuleiro
   */
  public TableGUI() {
    // Construtor sem par�metros requerido pela especifica�?o JavaBeans.
  }

  public TableGUI(JanelaPrincipal janela) {
    this.janela = janela;
    initComponents();
    criarCasas();
  }

  /**
   * Preenche o tabuleiro com 64 casas
   */
  private void criarCasas() {
    casas = new SquareGUI[8][8];
    // De cima para baixo
    for (int y = 7; y >= 0; y--) {
      // Da esquerda para a direita
      for (int x = 0; x < 8; x++) {
        Color cor = calcularCor(x, y);
        SquareGUI casa = new SquareGUI(x, y, cor, this);
        casas[x][y] = casa;
        add(casa);
      }
    }
  }

  private Color calcularCor(int x, int y) {
    // linha par
    if (x % 2 == 0) {
      // coluna �mpar
      if (y % 2 == 0) {
        return SquareGUI.COR_ESCURA;
      }
      // coluna �mpar
      else {
        return SquareGUI.COR_CLARA;
      }
    }
    // linha �mpar
    else {
      // coluna par
      if (y % 2 == 0) {
        return SquareGUI.COR_CLARA;
      }
      // coluna �mpar
      else {
        return SquareGUI.COR_ESCURA;
      }
    }

    // codigo acima em uma linha
    // return (i%2 + j%2)%2 == 0 ? CasaGUI.COR_ESCURA : CasaGUI.COR_CLARA;
  }

  public void atualizar(Game jogo) {
    for (int x = 0; x < 8; x++) {
      for (int y = 0; y < 8; y++) {
        SquareGUI casaGUI = casas[x][y];
        
        Table tabuleiro = jogo.getTable();
        Square casa = tabuleiro.getSquare(x, y);
        if (casa.hasPiece()) {
          Piece peca = casa.getPiece();

          if (peca instanceof Pawn) {
            if (peca.getColor() == Cor.WHITE) {
              casaGUI.desenharPedraBranca();
            } else if (peca.getColor() == Cor.BLACK) {
              casaGUI.desenharPedraPreta();
            }
          } else if (peca instanceof Dame) {
            if (peca.getColor() == Cor.WHITE) {
              casaGUI.desenharDamaBranca();
            } else if (peca.getColor() == Cor.BLACK) {
              casaGUI.desenharDamaPreta();
            }
          }
        }
        else {
          casaGUI.apagarPeca();
        }
      }
    }

    //Verificando se ainda há peças de duas cores no tabuleiro
    if (jogo.getTable().getTotalWhite() == 0) {
      JOptionPane.showMessageDialog(this, "As Damas Vermelhas ganharam o jogo");
    } else if(jogo.getTable().getTotalBlack() == 0){
      JOptionPane.showMessageDialog(this, "As Damas Brancas ganharam o jogo");
    }
  }

  public JanelaPrincipal getJanela() {
    return janela;
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    setLayout(new java.awt.GridLayout(8, 8));
  }// </editor-fold>//GEN-END:initComponents

  // Variables declaration - do not modify//GEN-BEGIN:variables
  // End of variables declaration//GEN-END:variables
}
