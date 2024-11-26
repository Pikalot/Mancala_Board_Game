import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class MancalaView extends JComponent implements ChangeListener {

    private ArrayList<Shape> shapes;
    private ArrayList<PitShape>	pits;
    private MancalaModel model;
    private FormatStrategy boardFormat;
    private int[] stoneArray;

    public MancalaView(MancalaModel model) {
        this.model = model;
        this.boardFormat = boardFormat;
        stoneArray = new int[14];
        pits = new ArrayList<>();
        boardFormat = this.model.getFormat();
        model.attach(this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (int i = 0; i < pits.size(); i++) {
                    if (pits.get(i).contains(e.getPoint()) && model.playable(i)) {
                        model.move(i);
                        return;
                    }
                }
            }
        });
    }

    public void addPit(PitShape shape) {
        pits.add(shape);
    }

    public void updateGame() {
        pits = new ArrayList<>();

        final int PIT_HEIGHT = 150;
        final int PIT_WIDTH = 80;
        final int TOP_Y = 30;
        final int BOT_Y = 290;

        PitShape pit0 = new PitShape(PIT_WIDTH + PIT_WIDTH/4, TOP_Y, PIT_WIDTH, PIT_HEIGHT);
        pit0.setShape(boardFormat.formatPitShape(pit0));
        pit0.setMarbles(stoneArray[0]);

        PitShape pit1 = new PitShape(2*PIT_WIDTH + PIT_WIDTH/2, TOP_Y, PIT_WIDTH, PIT_HEIGHT); //adds a pit 1
        pit1.setShape(boardFormat.formatPitShape(pit1));
        pit1.setMarbles(stoneArray[1]);

        PitShape pit2 = new PitShape(3*PIT_WIDTH + 3*PIT_WIDTH/4, TOP_Y, PIT_WIDTH, PIT_HEIGHT); //adds a pit 2
        pit2.setShape(boardFormat.formatPitShape(pit2));
        pit2.setMarbles(stoneArray[2]);

        PitShape pit3 = new PitShape(5*PIT_WIDTH, TOP_Y, PIT_WIDTH, PIT_HEIGHT); //adds a pit 3
        pit3.setShape(boardFormat.formatPitShape(pit3));
        pit3.setMarbles(stoneArray[3]);

        PitShape pit4 = new PitShape(6*PIT_WIDTH + PIT_WIDTH/4, TOP_Y, PIT_WIDTH, PIT_HEIGHT); //adds a pit 4
        pit4.setShape(boardFormat.formatPitShape(pit4));
        pit4.setMarbles(stoneArray[4]);

        PitShape pit5 = new PitShape(7*PIT_WIDTH + PIT_WIDTH/2, TOP_Y, PIT_WIDTH, PIT_HEIGHT); //adds a pit 5
        pit5.setShape(boardFormat.formatPitShape(pit5));
        pit5.setMarbles(stoneArray[5]);

        PitShape leftMancala = new PitShape(7, 60, PIT_WIDTH, 3*PIT_HEIGHT-100);
        leftMancala.setShape(boardFormat.formatPitShape(leftMancala));
        leftMancala.setMarbles(stoneArray[6]);

        //bottom pits
        PitShape pit7 = new PitShape(PIT_WIDTH + PIT_WIDTH/4, BOT_Y, PIT_WIDTH, PIT_HEIGHT);
        pit7.setShape(boardFormat.formatPitShape(pit7));
        pit7.setMarbles(stoneArray[7]);

        PitShape pit8 = new PitShape(2*PIT_WIDTH + PIT_WIDTH/2, BOT_Y, PIT_WIDTH, PIT_HEIGHT);
        pit8.setShape(boardFormat.formatPitShape(pit8));
        pit8.setMarbles(stoneArray[8]);

        PitShape pit9 = new PitShape(3*PIT_WIDTH + 3*PIT_WIDTH/4, BOT_Y, PIT_WIDTH, PIT_HEIGHT);
        pit9.setShape(boardFormat.formatPitShape(pit9));
        pit9.setMarbles(stoneArray[9]);

        PitShape pit10 = new PitShape(5*PIT_WIDTH, BOT_Y, PIT_WIDTH, PIT_HEIGHT);
        pit10.setShape(boardFormat.formatPitShape(pit10));
        pit10.setMarbles(stoneArray[10]);

        PitShape pit11 = new PitShape(6*PIT_WIDTH + PIT_WIDTH/4, BOT_Y, PIT_WIDTH, PIT_HEIGHT);
        pit11.setShape(boardFormat.formatPitShape(pit11));
        pit11.setMarbles(stoneArray[11]);

        PitShape pit12 = new PitShape(7*PIT_WIDTH + PIT_WIDTH/2, BOT_Y, PIT_WIDTH, PIT_HEIGHT);
        pit12.setShape(boardFormat.formatPitShape(pit12));
        pit12.setMarbles(stoneArray[12]);

        PitShape rightMancala = new PitShape(693 , 60, PIT_WIDTH, 3*PIT_HEIGHT-100);
        rightMancala.setShape(boardFormat.formatPitShape(rightMancala));
        rightMancala.setMarbles(stoneArray[13]);

        // Add Shapes in right order
        addPit(pit7);
        addPit(pit8);
        addPit(pit9);
        addPit(pit10);
        addPit(pit11);
        addPit(pit12);
        addPit(rightMancala);
        addPit(pit5);
        addPit(pit4);
        addPit(pit3);
        addPit(pit2);
        addPit(pit1);
        addPit(pit0);
        addPit(leftMancala);
    }

    @Override
    public void paintComponent(Graphics g) {

        g.drawImage(boardFormat.backgroundImg(), 0, 0, this);

        Graphics2D g2 = (Graphics2D) g;

//		for(Shape s: shapes) {
//			g2.draw(s);
//		}
        for(PitShape p: pits) {
            p.fill(g2);
        }

        //player information
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
        else if (model.getState() == GameState.COMPLETE) {
            turnInfo = "Final Score: ";
            finalScore = "Player A's " + model.getScoreCard(Player.A) +
                    " - Player B's " + model.getScoreCard(Player.B);
        }
        g.setColor(Color.WHITE);//Set g color to black for font
        g.setFont(new Font("Arial", Font.BOLD, 16));

        g2.drawString(turnInfo, (getWidth()/2)-200,getHeight()/2); //print turn information
        g2.drawString(finalScore,(getWidth()/2)-200,getHeight()/2+20);
    }

    public void setVisibility(boolean visibility) {
        this.setVisible(visibility);
    }

//    public void setBoardFormat(BoardFormat formatType) {
//        boardFormat = formatType;
//        updateGame();
//    }

    @Override
    public void stateChanged(ChangeEvent e) {
        updateGame();
        stoneArray = model.getPits();
        for(int i = 0; i < pits.size(); i++) {
            pits.get(i).setMarbles(stoneArray[i]);
        }
        repaint();
    }

    public void startGame() {
        setVisibility(true);
        model.setState(GameState.PLAYING);
    }
}