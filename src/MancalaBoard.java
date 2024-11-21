import javax.swing.*;
import java.awt.*;

public class MancalaBoard extends JFrame {
    private MancalaPit player1Mancala;
    private MancalaPit player2Mancala;
    private JPanel middlePitsPanel;

    //TEST
    public MancalaBoard() {
        setTitle("Mancala Game");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //Create Mancala pits with custom design
        player1Mancala = new MancalaPit("Player 1 Mancala", 0);
        player2Mancala = new MancalaPit("Player 2 Mancala", 0);

        //Middle pits panel setup
        middlePitsPanel = new JPanel();
        middlePitsPanel.setLayout(new GridLayout(2, 6, 10, 10));  // 2 rows of 6 pits

        //Add middle pits
        for (int i = 0; i < 6; i++) {
            middlePitsPanel.add(new MancalaPit("P1 Pit " + (i + 1), 4));
        }
        for (int i = 0; i < 6; i++) {
            middlePitsPanel.add(new MancalaPit("P2 Pit " + (i + 1), 4));
        }

        //Player 1 Mancala, Middle Pits, Player 2 Mancala
        add(player1Mancala, BorderLayout.WEST);
        add(middlePitsPanel, BorderLayout.CENTER);
        add(player2Mancala, BorderLayout.EAST);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MancalaBoard::new);
    }
}
