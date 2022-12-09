package Screens;

import Game.Game;
import Game.Table;
import Protocol.Message;
import client.Client;
import client.WaitMoveCallable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Tela do jogo.
 * Respons�vel por reagir aos cliques feitos pelo jogador.
 * 
 * @author Alan Moraes &lt;alan@ci.ufpb.br&gt;
 * @author Leonardo Villeth &lt;lvilleth@cc.ci.ufpb.br&gt;
 */
public class JanelaPrincipal extends JFrame {


  class WaitTask extends Thread {

    private Client player;
    private JanelaPrincipal windows;



    public WaitTask(Client player, JanelaPrincipal windows) {
      this.windows = windows;
      this.player = player;
    }

    @Override
    public void run() {
      try {
        boolean confirmTurn;
        do {
          Table newTable = this.player.waitMove();

          // Verificando se o turno continua o mesmo
          confirmTurn = newTable.isBlackRound() && windows.jogo.getTable().isBlackRound() || !newTable.isBlackRound() && !windows.jogo.getTable().isBlackRound();
          System.out.println("Turno continua igual: " + confirmTurn);
            if (newTable != null) {
              windows.jogo.setTabuleiro(newTable);
              windows.isListening = false;

              // Atualizando o tabuleiro
              atualizar();

              // Atualizando o label do turno
              if (windows.jogo.getTable().isBlackRound() && this.player.getColor().equals("BLACK") || !windows.jogo.getTable().isBlackRound() && this.player.getColor().equals("WHITE") )
                windows.turnoLabel.setText("Agora é sua vez!!");

              System.out.println("pediu");

    //          if (newTable.equals(windows.jogo.getTable())){
    //            (new WaitTask(this.player, windows)).start();
    //          }

            }
            // Limpando o listener
            this.windows.listener = null;
          } while (confirmTurn);
      } catch (ExecutionException e) {
        throw new RuntimeException(e);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

    }
  }

  private Game jogo;

  private Client player;
  private boolean isTurn;

  private boolean primeiroClique;
  private SquareGUI casaClicadaOrigem;
  private SquareGUI casaClicadaDestino;
  private boolean isListening = false;
  private WaitTask listener;

  /**
   * Responde aos cliques realizados no tabuleiro.
   * 
   * @param casaClicada Casa que o jogador clicou.
   */
  public void reagir(SquareGUI casaClicada) throws ExecutionException, InterruptedException {
    if (
      casaClicada.getCorPeca() == 0 && !this.player.getColor().equals("WHITE") ||
        casaClicada.getCorPeca() == 1 && !this.player.getColor().equals("BLACK")) {
      return;
    }

    if (primeiroClique) {
      if (casaClicada.possuiPeca()) {
        casaClicadaOrigem = casaClicada;
        casaClicadaOrigem.destacar();
        primeiroClique = false;
      }
      else {
        // clicou em uma posi�?o inv�lida, ent?o n?o faz nada.
        JOptionPane.showMessageDialog(this, "Clique em uma peça.");
      }
    }
    else {
//      player.getWaitToPlay().acquire();

      casaClicadaDestino = casaClicada;

      // Só mando a instrução para o servidor se for o turno do player
      if (this.jogo.getTable().isBlackRound() && this.player.getColor().equals("BLACK") || !this.jogo.getTable().isBlackRound() && this.player.getColor().equals("WHITE")) {

        Table newTable = this.player.move(casaClicadaOrigem.getPosicaoX(), casaClicadaOrigem.getPosicaoY(),
                casaClicadaDestino.getPosicaoX(), casaClicadaDestino.getPosicaoY(), jogo.getTable());

        System.out.println(newTable.equals(jogo.getTable()));

        jogo.setTabuleiro(newTable);

  //      jogo.moverPeca(casaClicadaOrigem.getPosicaoX(), casaClicadaOrigem.getPosicaoY(),
  //              casaClicadaDestino.getPosicaoX(), casaClicadaDestino.getPosicaoY());

      }

      casaClicadaOrigem.atenuar();
      primeiroClique = true;
      atualizar();

      // Só espera o tabuleiro uma e se não for o turno do player
      if (!this.isListening && (this.jogo.getTable().isBlackRound() && this.player.getColor().equals("WHITE") || !this.jogo.getTable().isBlackRound() && this.player.getColor().equals("BLACK"))) { // Esperando a joga do adversário
        // Agora o player vai começar a escutar
        this.isListening = true;

        // Ajustando as labels do player
        this.turnoLabel.setText("");
        this.jMenuBar1.removeAll();
        if (this.player.getColor().equals("WHITE"))
          menuArquivo.setText("Cor: Branco");
        else
          menuArquivo.setText("Cor: Vermelho");
        this.jMenuBar1.add(this.menuArquivo);
        this.jMenuBar1.add(this.turnoLabel);

        // Iniciando a escuta para pegar o novo tabuleiro
        System.out.println("Adversario vai jogar");
        // Só escuta um vez
        if (this.listener == null) {
          this.listener = new WaitTask(this.player, this);
          this.listener.start();
        }
      }
    }
  }
  

  /**
   * Construtor da classe.
   */
  public JanelaPrincipal(Client player, boolean isTurn) throws IOException, ClassNotFoundException, ExecutionException, InterruptedException {
    this.player = player;
    initComponents();

    this.setTitle("Dama Online");
    this.primeiroClique = true;
    this.casaClicadaOrigem = null;
    this.casaClicadaDestino = null;
    this.isTurn = isTurn;
    criarNovoJogo(isTurn);

    // configura action listener para o menu novo
    menuNovo.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        criarNovoJogo(isTurn);
      }
    });

    // configura action listener para o menu sair
    menuSair.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });

    if (!this.isListening && this.jogo.getTable().isBlackRound() && this.player.getColor().equals("WHITE") || !this.jogo.getTable().isBlackRound() && this.player.getColor().equals("BLACK")) { // Esperando a joga do adversário
      // Agora o player vai começar a escutar
      this.isListening = true;

      // Ajustando as labels do player
      this.turnoLabel.setText("");
      this.jMenuBar1.removeAll();
      if (this.player.getColor().equals("WHITE"))
        menuArquivo.setText("Cor: Branco");
      else
        menuArquivo.setText("Cor: Vermelho");
      this.jMenuBar1.add(this.menuArquivo);
      this.jMenuBar1.add(this.turnoLabel);

      // Iniciando a escuta para pegar o novo tabuleiro
      System.out.println("Adversario vai jogar");

      // Só escuta uma vez
      if (this.listener == null) {
        this.listener = new WaitTask(this.player, this);
        this.listener.start();
      }

    }

    super.setLocationRelativeTo(null);
    super.setVisible(true);
    super.pack();
  }

  

  /**
   * Cria um novo jogo e atualiza o tabuleiro gr�fico.
   */
  private void criarNovoJogo(boolean isTurn) {
    if(!primeiroClique) {
      primeiroClique = true;
      casaClicadaOrigem.atenuar();
    }         
    jogo = new Game(isTurn);
    atualizar();
  }

  private void atualizar() {
    tabuleiroGUI.atualizar(jogo);
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    pnlLinhas = new javax.swing.JPanel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    jLabel7 = new javax.swing.JLabel();
    jLabel8 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel1 = new javax.swing.JLabel();
    pnlColunas = new javax.swing.JPanel();
    lbl_a = new javax.swing.JLabel();
    lbl_b = new javax.swing.JLabel();
    lbl_c = new javax.swing.JLabel();
    lbl_d = new javax.swing.JLabel();
    lbl_e = new javax.swing.JLabel();
    lbl_f = new javax.swing.JLabel();
    lbl_g = new javax.swing.JLabel();
    lbl_h = new javax.swing.JLabel();
    tabuleiroGUI = new TableGUI(this);
    jMenuBar1 = new javax.swing.JMenuBar();
    menuArquivo = new javax.swing.JMenu();
    turnoLabel = new javax.swing.JMenu();
    menuNovo = new javax.swing.JMenuItem();
    jSeparator1 = new javax.swing.JPopupMenu.Separator();
    menuSair = new javax.swing.JMenuItem();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    pnlLinhas.setLayout(new java.awt.GridLayout(8, 1));

    jLabel3.setFont(new java.awt.Font("Abyssinica SIL", 0, 18)); // NOI18N
    jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel3.setText("7");
    pnlLinhas.add(jLabel3);

    jLabel4.setFont(new java.awt.Font("Abyssinica SIL", 0, 18)); // NOI18N
    jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel4.setText("6");
    pnlLinhas.add(jLabel4);

    jLabel5.setFont(new java.awt.Font("Abyssinica SIL", 0, 18)); // NOI18N
    jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel5.setText("5");
    pnlLinhas.add(jLabel5);

    jLabel6.setFont(new java.awt.Font("Abyssinica SIL", 0, 18)); // NOI18N
    jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel6.setText("4");
    pnlLinhas.add(jLabel6);

    jLabel7.setFont(new java.awt.Font("Abyssinica SIL", 0, 18)); // NOI18N
    jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel7.setText("3");
    pnlLinhas.add(jLabel7);

    jLabel8.setFont(new java.awt.Font("Abyssinica SIL", 0, 18)); // NOI18N
    jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel8.setText("2");
    pnlLinhas.add(jLabel8);

    jLabel2.setFont(new java.awt.Font("Abyssinica SIL", 0, 18)); // NOI18N
    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setText("1");
    pnlLinhas.add(jLabel2);

    jLabel1.setFont(new java.awt.Font("Abyssinica SIL", 0, 18)); // NOI18N
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("0");
    pnlLinhas.add(jLabel1);

    pnlColunas.setLayout(new java.awt.GridLayout(1, 8));

    lbl_a.setFont(new java.awt.Font("Arimo", 0, 18)); // NOI18N
    lbl_a.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lbl_a.setText("0");
    pnlColunas.add(lbl_a);

    lbl_b.setFont(new java.awt.Font("Arimo", 0, 18)); // NOI18N
    lbl_b.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lbl_b.setText("1");
    pnlColunas.add(lbl_b);

    lbl_c.setFont(new java.awt.Font("Arimo", 0, 18)); // NOI18N
    lbl_c.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lbl_c.setText("2");
    pnlColunas.add(lbl_c);

    lbl_d.setFont(new java.awt.Font("Arimo", 0, 18)); // NOI18N
    lbl_d.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lbl_d.setText("3");
    pnlColunas.add(lbl_d);

    lbl_e.setFont(new java.awt.Font("Arimo", 0, 18)); // NOI18N
    lbl_e.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lbl_e.setText("4");
    pnlColunas.add(lbl_e);

    lbl_f.setFont(new java.awt.Font("Arimo", 0, 18)); // NOI18N
    lbl_f.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lbl_f.setText("5");
    pnlColunas.add(lbl_f);

    lbl_g.setFont(new java.awt.Font("Arimo", 0, 18)); // NOI18N
    lbl_g.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lbl_g.setText("6");
    pnlColunas.add(lbl_g);

    lbl_h.setFont(new java.awt.Font("Arimo", 0, 18)); // NOI18N
    lbl_h.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lbl_h.setText("7");
    pnlColunas.add(lbl_h);

    if (this.player.getColor().equals("WHITE"))
      menuArquivo.setText("Cor: Branco");
    else
      menuArquivo.setText("Cor: Vermelho");

//    menuNovo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
//    menuNovo.setText("Novo");
//    menuArquivo.add(menuNovo);
//    menuArquivo.add(jSeparator1);

    menuSair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
    menuSair.setText("Sair");
    menuArquivo.add(menuSair);

    jMenuBar1.add(menuArquivo);
    jMenuBar1.add(turnoLabel);
    setJMenuBar(jMenuBar1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(pnlLinhas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(pnlColunas, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE)
          .addComponent(tabuleiroGUI, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(10, 10, 10)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(pnlLinhas, javax.swing.GroupLayout.PREFERRED_SIZE, 576, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(tabuleiroGUI, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(pnlColunas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  /**
   * @param args the command line arguments
   */
  public static void startGame(Client player) {

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          new JanelaPrincipal(player, true).setVisible(true);
        } catch (IOException e) {
          throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
          throw new RuntimeException(e);
        } catch (ExecutionException e) {
          throw new RuntimeException(e);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JMenuBar jMenuBar1;
  private javax.swing.JPopupMenu.Separator jSeparator1;
  private javax.swing.JLabel lbl_a;
  private javax.swing.JLabel lbl_b;
  private javax.swing.JLabel lbl_c;
  private javax.swing.JLabel lbl_d;
  private javax.swing.JLabel lbl_e;
  private javax.swing.JLabel lbl_f;
  private javax.swing.JLabel lbl_g;
  private javax.swing.JLabel lbl_h;
  private javax.swing.JMenu menuArquivo;
  private javax.swing.JMenu turnoLabel;
  private javax.swing.JMenuItem menuNovo;
  private javax.swing.JMenuItem menuSair;
  private javax.swing.JPanel pnlColunas;
  private javax.swing.JPanel pnlLinhas;
  private TableGUI tabuleiroGUI;
  // End of variables declaration//GEN-END:variables

}
