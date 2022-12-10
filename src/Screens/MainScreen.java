package Screens;

import Protocol.Message;
import client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

public class MainScreen extends JFrame{
    private JTextField codeTextField;
    private JPanel panelMain;
    private JButton entrarButton;
    private JButton criaSalaButton;
    private JLabel erroLabel;

    private String roomCode = "";

    private Client player;

    public MainScreen() {
        // configurando a tela
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Dama Online");
        this.entrarButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.criaSalaButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Lendo os dados do input do código da sala
        codeTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) { // Tecla liberada
                super.keyTyped(e);
                // limpando o label de erro
                erroLabel.setText(" ");
                roomCode = codeTextField.getText();
            }
        });

        // Evento de click para entrar na sala
        entrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Cria o cliente não existir
                    if (player == null){ // Matém a mesma conexão
                        player = new Client();
                    }

                    // Cor padrão preta para o segundo jogador
                    player.setColor("BLACK");
                    boolean isConnected = player.connectSession(roomCode);

                    // Validando a conexão
                    if (isConnected) {
                        // player.setWaitToPlay(new Semaphore(0, true)); // entra sem poder mover as peças

                        // Nova tela do tabuleiro
                        final JanelaPrincipal playFrame = new JanelaPrincipal(player, false);
                        close(); // Fechando a tela antiga
                        System.out.println("Code: " + player.getCodeSession());
                    } else {
                        // Setando o erro caso a sala não exista
                        erroLabel.setText("Erro ao se conectar com a sala: " + roomCode);
                        if (roomCode.equals("")) {
                            erroLabel.setText("           Digite o código de uma sala " + roomCode);
                        }

                        erroLabel.setVisible(true);

                    }

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ExecutionException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Evento de click para criar uma sala
        criaSalaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println("Click");
                try {
                    if (player == null){ // Criando a primera sala ao entrar no jogo e criando um cliente também
                        player = new Client();
                        player.setColor("WHITE"); // Cor padrão para jogadores que criam uma sala
                        player.createSession();

                        // Abrindo a nova tela de espera por outro jogador
                        final LoadingScreen loadingFrame = new LoadingScreen(player);
                        System.out.println("Code: " + player.getCodeSession());
                        close();// Fechando a antiga tela
                    } else {
                        player.createSession();
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ExecutionException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public static void main(String[] args) {
        // Inciando a tela de inicio
        MainScreen mainScreen = new MainScreen();
        mainScreen.setContentPane(mainScreen.panelMain);
        mainScreen.setSize(1260,760);
        mainScreen.setTitle("Dama Online");
        mainScreen.setVisible(true);
        mainScreen.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainScreen.setLayout(mainScreen.getLayout());
    }

    public void close(){ // fecha a tela atual
        this.dispose();
    }

}
