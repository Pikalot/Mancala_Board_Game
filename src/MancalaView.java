import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * A concrete class displays the board and pits.
 * This class serves as an observer in the MVC (Model-View-Controller) architecture
 * for displaying a list of stone pits. It listens to changes in the model and updates
 * the stones accordingly.
 * A MancalaView has an associated Mancala Model and Format Strategy.
 *
 * @author Tuan-Anh
 * @version 1.1 12/05/2024
 */
public class MancalaView extends JComponent implements ChangeListener, MancalaController {
    private ArrayList<StonePit> pits;
    private MancalaModel model;
    private FormatStrategy boardFormat;
    private int[] stoneArray;
    private int selectedPit;

    /** Constructs a MancalaView with a specified model.*/
    public MancalaView(MancalaModel model) {
        this.model = model;
        stoneArray = new int[14];
        pits = new ArrayList<>();
        boardFormat = this.model.getFormat();
        model.attach(this);
        selectedPit = -1;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            for (int i = 0; i < pits.size(); i++) {
                if (pits.get(i).isSelected()) pits.get(i).setSelected(false);
                if (pits.get(i).contains(e.getPoint()) && model.playable(i)) {
                    selectedPit = i;
                    pits.get(i).setSelected(true);
                    notifyModel();
                    return;
                }
            }
            repaint();
            }
        });
    }

    /**
     * Adds a stone pit shape to the board.
     * @param shape a stone pit shape
     */
    public void addPit(StonePit shape) {
        pits.add(shape);
    }

    /**
     * Updates the board by recreating pits
     * and Mancalas based on the game model's state and stone counts.
     */
    public void updateGame() {
        pits = new ArrayList<>();
        final int PIT_HEIGHT = 150;
        final int PIT_WIDTH = 80;
        final int TOP_Y = 30;
        final int BOT_Y = 290;

        for (int i = 0; i < model.MAX_PITS; i++) {
            // Pits A
            if (i < model.PLAYER_A_PIT) {
                StonePit pit = new StonePit((i + 1) * (PIT_WIDTH + PIT_WIDTH/4) + PIT_WIDTH/2, BOT_Y, PIT_WIDTH, PIT_HEIGHT);
                pit.setNumberOfStones(stoneArray[i]);
                addPit(pit);
                // Preserve selection state
                if (i == selectedPit) {
                    pit.setSelected(true);
                }
            }

            // Mancala A
            else if (i == model.PLAYER_A_PIT) {
                StonePit pit = new StonePit(7 * (PIT_WIDTH + PIT_WIDTH/4) + PIT_WIDTH/2, 60, PIT_WIDTH, 3*PIT_HEIGHT-100); // Mancala B
                addPit(pit);
            }

            // Pit B
            else if (i < model.PLAYER_B_PIT) {
                StonePit pit = new StonePit((13 - i) * (PIT_WIDTH + PIT_WIDTH/4) + PIT_WIDTH/2, TOP_Y, PIT_WIDTH, PIT_HEIGHT);
                pit.setNumberOfStones(stoneArray[i]);
                addPit(pit);
                // Preserve selection state
                if (i == selectedPit) {
                    pit.setSelected(true);
                }
            }

            // Mancala B
            else {
                StonePit pit = new StonePit( PIT_WIDTH/2, 60, PIT_WIDTH, 3*PIT_HEIGHT-100);
                addPit(pit);
            }
        }
    }

    /** Customizes the painting of this component by implementing FormatStrategy.
     *
     * @param g the <code>Graphics</code> object to paint.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g.drawImage(boardFormat.backgroundImg(), 0, 0, this);

        // Show player's turns
        String turnInfo = "";
        String finalScore = "";

        if (model.getState() == GameState.PLAYING) {
            if (model.getPlayer() == Player.A) {
                turnInfo = "Player A's Turn";
            }
            else {
                turnInfo = "Player B's Turn";
            }
        }

        if (model.getState() == GameState.COMPLETE) {
            turnInfo = "Final Score: ";
            finalScore = "Player A's " + model.getScoreCard(Player.A) +
                    " - Player B's " + model.getScoreCard(Player.B);
        }

        g2.setColor(boardFormat.getColor()); // Set text color
        g2.setFont(boardFormat.getFont());
        g2.drawString(turnInfo, (getWidth()/2)-200,getHeight()/2); //print turn information
        g2.drawString(finalScore,(getWidth()/2)-200,getHeight()/2+20);

        for(int i = 0; i < pits.size(); i++) {
            StonePit currentPit = this.pits.get(i);
            StringBuilder label = new StringBuilder();
            int rotateY = currentPit.getY() + currentPit.getHeight()* 2/3;

            currentPit.draw(g2);
            g2.setColor(boardFormat.getColor());
            g2.setFont(boardFormat.getFont());

            if (currentPit.isSelected()) {
                currentPit.drawSelection(g2);
            }

            if (i <= model.PLAYER_A_PIT) {
                label.append("A");
                if (i == model.PLAYER_A_PIT) {
                    int rotateX = currentPit.getX() + currentPit.getWidth() + 25;
                    label.insert(0, "MANCALA  ");
                    g2.rotate(-Math.PI / 2, rotateX, rotateY);
                    g.drawString(label.toString(), rotateX, rotateY);
                    g2.rotate(Math.PI / 2, rotateX, rotateY);
                }
                else {
                    label.append(i + 1);
                    g2.drawString(label.toString(), currentPit.getX() + currentPit.getWidth()/3, currentPit.getY() + currentPit.getHeight() + 20);
                }
            }
            else {
                label.append("B");
                if (i == model.PLAYER_B_PIT) {
                    int rotateX = currentPit.getX() - 10;
                    label.insert(0, "MANCALA  ");
                    g2.rotate(-Math.PI / 2, rotateX, rotateY);
                    g.drawString(label.toString(), rotateX, rotateY);
                    g2.rotate(Math.PI / 2, rotateX, rotateY);
                } else {
                    label.append(i - 6);
                    g2.setColor(boardFormat.getColor());
                    g2.drawString(label.toString(), currentPit.getX() + currentPit.getWidth()/3, currentPit.getY() - 5);
                }
            }
        }
    }

    /**
     * Sets the visibility of the game board.
     * @param visibility true for showing the board and false for hiding the board
     */
    public void setVisibility(boolean visibility) {
        this.setVisible(visibility);
    }

    /**
     * Updates the view whenever the model notifies it of changes.
     * This method is part of the Observer pattern.
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        setVisibility(true);
        updateGame();
        this.boardFormat = model.getFormat();
        for (StonePit pit: pits) {
            boardFormat.formatShape(pit);
        }
        stoneArray = model.getPits();
        for(int i = 0; i < pits.size(); i++) {
            pits.get(i).setNumberOfStones(stoneArray[i]);
        }
        repaint();
    }

    /** Notifies the model when a player makes a move. */
    @Override
    public void notifyModel() {
        model.move(selectedPit);
    }
}