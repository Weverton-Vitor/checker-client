import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.border.*;

public class MainScreen extends JFrame{
    private JTextField codeTextField;
    private JPanel panelMain;
    private JButton entrarButton;
    private JButton criaSalaButton;

    private String roomCode;

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
                System.out.println("Enviando: " + roomCode);
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

}
