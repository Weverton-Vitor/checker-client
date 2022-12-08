package Screens;

import Game.Game;
import client.Client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class LoadingScreen extends JFrame {

    class WaitTask extends Thread {

        private Client player;

        public WaitTask(Client player) {
            this.player = player;
        }

        @Override
        public void run() {
            try {
                player.waitConnection();
                final JanelaPrincipal playFrame = new JanelaPrincipal(player, false);
                close();


            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private JPanel loadingScreen;
    private JLabel loadingGif;
    private JLabel codeLable;

    private Client player;

    public LoadingScreen(Client player) throws HeadlessException, ExecutionException, InterruptedException {
//        LoadingScreen loadingScreen = new LoadingScreen();
        System.out.println(player);
        this.codeLable.setText("Código Da Sala: " + player.getCodeSession());


        JLabel background=new JLabel("", new ImageIcon("tumblr_mwg7x2Kfhg1rat0tqo1_500.gif"), JLabel.CENTER);
        background.setBounds(0, 0, 1200, 700);
        add(background);

        this.player = player;
//        this.setContentPane(this.loadingScreen);
        this.setSize(1260,760);
        this.setTitle("Dama Online");
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(loadingScreen.getLayout());
        (new LoadingScreen.WaitTask(this.player)).start();

    }

//    public static void render(Client player) {
//        LoadingScreen loadingScreen = new LoadingScreen();
//        loadingScreen.setPlayer(player);
//        loadingScreen.setContentPane(loadingScreen.loadingScreen);
//        loadingScreen.setSize(1260,760);
//        loadingScreen.setTitle("Dama Online");
//        loadingScreen.setVisible(true);
//        loadingScreen.setDefaultCloseOperation(EXIT_ON_CLOSE);
//        loadingScreen.setLayout(loadingScreen.getLayout());
//
//
//    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public void close(){
        this.dispose();
    }

    public Client getPlayer() {
        return player;
    }

    public void setPlayer(Client player) {
        this.player = player;
    }
}
