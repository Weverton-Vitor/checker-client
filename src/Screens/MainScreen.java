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

public class MainScreen extends JFrame{
    private JTextField codeTextField;
    private JPanel panelMain;
    private JButton entrarButton;
    private JButton criaSalaButton;

    private String roomCode;

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
                roomCode = codeTextField.getText();
            }
        });

        entrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (player == null){ // Criando a primera sala ao entrar no jogo
                        player = new Client();
                        player.connectSession(roomCode);

                        final JanelaPrincipal loadingFrame = new JanelaPrincipal(player);
                        close();
                        System.out.println("Code: " + player.getCodeSession());
                    } else {
                        //TODO criar sala depois de "logado"
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ExecutionException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                // System.out.println("Enviando: " + roomCode);
                // Message message = new Message();
                // message.setAction("CONNECT_SESSION");
                // message.setCodeSession(roomCode);
                // player.
            }
        });


        criaSalaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Click");
                try {
                    if (player == null){ // Criando a primera sala ao entrar no jogo
                        player = new Client();
                        player.createSession();

                        final JanelaPrincipal loadingFrame = new JanelaPrincipal(player);

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

}
