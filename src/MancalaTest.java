/*------------------------------------------------------------------------------------------------------------------------
Student Name: Tuan-Anh Ho, Anthony Warsah Liu, Ahsan Ali
Team: AAA
Course: CS 151
Term/Year: Fall 2024
Date: 12/05/2024
Assignment Number: Team Project
------------------------------------------------------------------------------------------------------------------------*/
import javax.swing.*;

/**
 * A program allows two players to play a Mancala board game.
 *
 * @author Tuan-Anh Ho
 * @version 1.1 12/05/2024
 */
public class MancalaTest {
    public static void main(String[] args) {
        MancalaModel model = new MancalaModel();
        MancalaBoard mFrame = new MancalaBoard(model);
        mFrame.setVisible(true);
        mFrame.setLocation(450, 250);
        mFrame.setTitle("Mancala Game");
        mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}