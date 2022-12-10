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
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("Dama Online");

        this.entrarButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.criaSalaButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

//        this.entrarButton.setBorder(new Border());

        codeTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyTyped(e);
                erroLabel.setText(" ");
                roomCode = codeTextField.getText();
            }
        });

        entrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (player == null){ // Matém a mesma conexão
                        player = new Client();

                    }

                    player.setColor("BLACK");
                    boolean isConnected = player.connectSession(roomCode);
                    System.out.println(isConnected);
                    if (isConnected) {
                        // player.setWaitToPlay(new Semaphore(0, true)); // entra sem poder mover as peças
                        final JanelaPrincipal playFrame = new JanelaPrincipal(player, false);
                        close();
                        System.out.println("Code: " + player.getCodeSession());
                    } else {
                            erroLabel.setText("Erro ao se conectar com a sala: " + roomCode);

                        if (roomCode.equals(""))
                            erroLabel.setText("           Digite o código de uma sala " + roomCode);

                            erroLabel.setVisible(true);
//                            updateScreen();
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


        criaSalaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Click");
                try {
                    if (player == null){ // Criando a primera sala ao entrar no jogo
                        player = new Client();
                        player.setColor("WHITE");
                        player.createSession();
                        final LoadingScreen loadingFrame = new LoadingScreen(player);

                        System.out.println("Code: " + player.getCodeSession());
                        close();
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
        MainScreen mainScreen = new MainScreen();
        mainScreen.setContentPane(mainScreen.panelMain);
        mainScreen.setSize(1260,760);
        mainScreen.setTitle("Dama Online");
        mainScreen.setVisible(true);
        mainScreen.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainScreen.setLayout(mainScreen.getLayout());
    }

    public void close(){
        this.dispose();
    }

    public void updateScreen(){
        this.repaint();
        this.validate();
    }

}
