package Screens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Interface Grafica de uma Casa no tabuleiro do jogo.
 *
 * @author Alan Moraes &lt;alan@ci.ufpb.br&gt;
 * @author Leonardo Villeth &lt;lvilleth@cc.ci.ufpb.br&gt;
 */
public class SquareGUI extends JButton {

  // Constantes 
  public static final Color COR_CLARA = new Color(182, 155, 76);
  public static final Color COR_ESCURA = new Color(65, 41, 1);
  private static final Color COR_DESTAQUE = new Color(0, 1, 0, 0.4f);
  
  // Icones das pecas
  private static final Icon PEDRA_BRANCA = new ImageIcon("./imagens/pedra_branca.png");
  private static final Icon PEDRA_PRETA = new ImageIcon("./imagens/pedra_vermelha.png");
  private static final Icon DAMA_BRANCA = new ImageIcon("./imagens/dama_branca.png");
  private static final Icon DAMA_PRETA = new ImageIcon("./imagens/dama_vermelha.png");
  
  // Cores das pecas
  public static final int SEM_PECA = -1;
  public static final int PECA_BRANCA = 0;
  public static final int PECA_PRETA = 1;
  

  private int x;
  private int y;
  private Color cor;

  public SquareGUI(int x, int y, Color cor, TableGUI tabuleiro) {
    this.x = x;
    this.y = y;
    this.cor = cor;
    setIcon(null);

    // Layout e cor
    setBackground(cor);
    setOpaque(false);
    setBorder(BorderFactory.createLineBorder(cor, 1));
    setContentAreaFilled(false);

    addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        tabuleiro.getJanela().reagir((SquareGUI) e.getSource());
      }
    });
  }

  public int getPosicaoX() {
    return x;
  }

  public int getPosicaoY() {
    return y;
  }

  public void desenharPedraBranca() {
    setIcon(SquareGUI.PEDRA_BRANCA);
  }

  public void desenharDamaBranca() {
    setIcon(SquareGUI.DAMA_BRANCA);
  }

  public void desenharDamaPreta() {
    setIcon(SquareGUI.DAMA_PRETA);
  }

  public void desenharPedraPreta() {
    setIcon(SquareGUI.PEDRA_PRETA);
  }
  public void apagarPeca() {
    setIcon(null);
  }

  public boolean possuiPeca() {
    return getIcon() != null;
  }
  
  public int getCorPeca() {
    Icon icone = getIcon();
    ArrayList<Icon> pecasBrancas = new ArrayList<Icon>(6); // AGRUPA OS ICONES DAS PEÇAS BRANCAS
    ArrayList<Icon> pecasPretas = new ArrayList<Icon>(6); // AGRUPA OS ICONES DAS PEÇAS PRETAS

    pecasBrancas.add(SquareGUI.PEDRA_BRANCA);
    pecasBrancas.add(SquareGUI.DAMA_BRANCA);

    pecasPretas.add(SquareGUI.PEDRA_PRETA);
    pecasPretas.add(SquareGUI.DAMA_PRETA);

    if (pecasBrancas.contains(icone)) {
      return PECA_BRANCA;
    }
    else if (pecasPretas.contains(icone)) {
       return PECA_PRETA;
    }
    else {
      return SEM_PECA;
    }
  }
  
  public void destacar() {
    setBackground(COR_DESTAQUE);
  }

  public void atenuar() {
    setBackground(cor);
  }

  /**
   * Pinta o componente com a cor de fundo, aceita valores RGBA
   */
  @Override
  protected void paintComponent(Graphics g) {
    g.setColor(getBackground());
    g.fillRect(0, 0, getWidth(), getHeight());
    super.paintComponent(g);
  }

}
