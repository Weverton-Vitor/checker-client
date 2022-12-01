package Screens;

import javax.swing.*;
import java.awt.*;

public class LoadingScreen extends JFrame {
    private JPanel loadingScreen;

    public LoadingScreen() throws HeadlessException {

    }

    public static void render() {
        LoadingScreen loadingScreen = new LoadingScreen();
        loadingScreen.setContentPane(loadingScreen.loadingScreen);
        loadingScreen.setSize(1260,760);
        loadingScreen.setTitle("Dama Online");
        loadingScreen.setVisible(true);
        loadingScreen.setDefaultCloseOperation(EXIT_ON_CLOSE);
        loadingScreen.setLayout(loadingScreen.getLayout());
    }
}
