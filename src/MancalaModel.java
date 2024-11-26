import javax.swing.event.ChangeEvent;
import java.util.ArrayList;

public class MancalaModel {
    private ArrayList<MancalaView> listeners;
    private int[] pits;
    private int[] prevPits;
    private Player player;
    private GameState state;
    private boolean isLastStone;
    private boolean isUndoable;
    private int undoCount1;
    private int undoCount2;
    private FormatStrategy format;

    public MancalaModel(){
        setListeners(new ArrayList<>());
        setPits(new int[getMaxPits()]);
        setPrevPits(new int[getMaxPits()]);
        setState(GameState.BEGIN);
        setPlayer(Player.A);
        setUndoCount1(0);
        setUndoCount2(0);
        setLastStone(false);
        setUndoable(false);
    }

    private void notifyListeners() {
        for (MancalaView l : getListeners()){
            l.stateChanged(new ChangeEvent(this));
        }
    }


    public void setStones(int stones) {
        for(int i = 0; i < getPits().length; i++) {
            if (!isInPlayerPit(i))
                getPits()[i] = stones;
        }
        this.notifyListeners();
    }

    public void attach(MancalaView listener) {
        getListeners().add(listener);
    }

    public static int getMaxPits() {
        return 14;
    }

    public static int getPlayerPitA() {
        return 6;
    }

    public static int getPlayerPitB() {
        return 13;
    }



    private Player getCurrentPlayer(int pit) {
        if (pit >= 0 && pit <= getPlayerPitA()) {
            return Player.A;
        }
        else return Player.B;
    }


    private boolean isInPlayerPit(int pit) {
        return (pit == getPlayerPitA() || pit == getPlayerPitB());
    }


    private int getOppositePit(int pit) {
        if(pit <=12) {
            return 12 - pit;
        } else{
            return pit - 12;
        }
    }


    public boolean playable(int pit) {
        // Check if the game is in a playable state
        if (getState() != GameState.PLAYING) {
            return false;
        }

        // Ensure the pit is not empty
        if (getPits()[pit] == 0) {
            return false;
        }

        // Verify it's the current player's pit
        if (getCurrentPlayer(pit) != getPlayer()) {
            return false;
        }

        // Validate that the pit is within bounds and not a Mancala
        return pit < getMaxPits() && pit != getPlayerPitA() && pit != getPlayerPitB();
    }

    private void save() {
        setPrevPits(getPits().clone());
    }

    /**
     *changePlayer changes the turn
     */
    private void switchPlayer() {

        if (getPlayer() == Player.A) {
            setPlayer(Player.B);
        }
        else {
            setPlayer(Player.A);
        }
    }

    private void moveLastStoneToMancala() {
        for (int i = 0; i < getMaxPits(); ++i) {
            if (!isInPlayerPit(i)) { // Skip the Mancala pit
                int stones = getPits()[i];
                if (stones > 0) {
                    getPits()[getPlayerMancala(getCurrentPlayer(i))] += stones; // Move all stones to a player pit
                    getPits()[i] = 0; // Take all stone from current pit
                }
            }
        }
    }

    private int getPlayerMancala(Player player) {
        return player == Player.A ? getPlayerPitA() : getPlayerPitB();
    }

    private void doLastStoneRule(int pit) {
        // last stone lands on own Mancala
        if (isOwnMancala(pit)) {
            setLastStone(true); // Free move
            return;
        }

        // last stone lands on an empty pit
        if (isOwnEmptyPit(pit)) {
            stealStones(pit);
        }
        setLastStone(false);
        switchPlayer();

    }

    private boolean isOwnMancala(int pit) {
        return getCurrentPlayer(pit) == getPlayer() && isInPlayerPit(pit);
    }

    private boolean isOwnEmptyPit(int pit) {
        return getCurrentPlayer(pit) == getPlayer() &&
                getPits()[pit] == 1 &&
                getPits()[getOppositePit(pit)] > 0;
    }

    private void stealStones(int currentPit) {
        int stones = getPits()[currentPit] + getPits()[getOppositePit(currentPit)];
        getPits()[currentPit] = 0;
        getPits()[getOppositePit(currentPit)] = 0;
        // Find the player pit
        int mancala = (getPlayer() == Player.A ? getPlayerPitA() : getPlayerPitB());
        getPits()[mancala] += stones; // Pass all stone to that player pit
    }


    private boolean isWin() {
        int pAPits = 0;
        int pBPits = 0;

        for (int i = 0; i < getPlayerPitA(); i++) {
            pAPits += getPits()[i];
        }

        for (int i = getPlayerPitA() + 1; i < getPlayerPitB(); i++) {
            pBPits += getPits()[i];
        }

        return (pAPits == 0 || pBPits == 0);
    }


    public void move(int pitIndex) {
        // save state
        this.save();

        setUndoable(true);
        resetUndoCount();
        // Remove all stones from the selected pit
        int stones = getPits()[pitIndex];
        getPits()[pitIndex] = 0;

        // Distribute the stones to adjacent pits and do last stone rule
        int currentPit = dropStones(pitIndex, stones);
        doLastStoneRule(currentPit);

        // Check if the game is complete (all pits on one side are empty)
        if (isWin()) {
            moveLastStoneToMancala();
            state = GameState.COMPLETE;
        }

        // Notify all listeners of the updated game state
        notifyListeners();
    }

    /**
     * Resets the undo count for players.
     */
    private void resetUndoCount() {
        if (getPlayer().equals(Player.A)) {
            setUndoCount1(0);
//            System.out.println(getPlayer().toString());
//            System.out.println(getUndoCount1() + " " + getUndoCount2());
        } else {
            setUndoCount2(0);
        }
    }

    private int dropStones(int startPit, int stones) {
        int currentPit = startPit;

        while (stones > 0) {
            currentPit = (currentPit + 1) % getMaxPits(); // Wrap around the board
//            System.out.println(currentPit);
            // Skip the opponent's Mancala
            if (isInPlayerPit(currentPit) && getCurrentPlayer(currentPit) != getPlayer()) {
                continue;
            }

            // Place one stone in the current pit
            getPits()[currentPit]++;
            stones--;
        }

        return currentPit;
    }

    /**
     * undoMove undo's the last move if possible.
     */
//    public void undoMove() {
//        boolean flag = false;
//
//        if (!isUndoable()){
//            return;
//        }
//
//        switch (getPlayer()) {
//
//            case A:
//            {
//                if (isLastStone() && getUndoCount1() < maxUndo())
//                {
//                    setUndoCount1(getUndoCount1() + 1);
//                    flag = true;
//                }
//                else if (!isLastStone() && getUndoCount2() < maxUndo())
//                {
//                    setUndoCount2(getUndoCount2() + 1);
//                    setPlayer(Player.B);
//                    flag = true;
//                }
//                break;
//            }
//            case B:
//            {
//                if (isLastStone() && getUndoCount1() < maxUndo())
//                {
//                    setUndoCount2(getUndoCount2() + 1);
//                    flag = true;
//                }
//                else if (!isLastStone() && getUndoCount1() < maxUndo())
//                {
//                    setUndoCount1(getUndoCount1() + 1);
//                    flag = true;
//                    setPlayer(Player.A);
//                }
//                break;
//            }
//        }
//
//        if ((getUndoCount1() < maxUndo() && getState().equals(GameState.COMPLETE))  ||  (getUndoCount2() < maxUndo() && getState().equals(GameState.COMPLETE))) {
//            state = GameState.PLAYING;
//        }
//
//        //able to undo
//        if (flag) {
//            setPits(getPrevPits().clone());
//            setUndoable(false);
//            notifyListeners();
//        }
//    }

    public void undoMove() {
        // If undo is not allowed, exit early
        if (!isUndoable()) {
            return;
        }

        boolean flag = false;

        // Handle undo logic based on the current player
        if (undoable(getPlayer())) {
            incrementUndoCount(getPlayer());

            // Switch player if previous move belongs to the other player
            if (!isLastStone()) {
                switchPlayer();
            }

            flag = true;
        }

        // Update game state if required
        if (isEndgameUndo()) {
            state = GameState.PLAYING;
        }

        // Perform the undo operation
        if (flag) {
            setPits(getPrevPits().clone());
            setUndoable(false);
            notifyListeners(); // Update the game state in the UI
        }
    }

    private boolean undoable(Player player) {
        int undoCount = (player == Player.A) ? getUndoCount1() : getUndoCount2();
        return undoCount < maxUndo();
    }

    /**
     * Increments the undo count for the specified player.
     */
    private void incrementUndoCount(Player player) {
        if (player == Player.A) {
            setUndoCount1(getUndoCount1() + 1);
        } else {
            setUndoCount2(getUndoCount2() + 1);
        }
    }

    /**
     * Checks if the game state should continue after an undo.
     */
    private boolean isEndgameUndo() {
        return (getUndoCount1() < maxUndo() || getUndoCount2() < maxUndo())
                && getState().equals(GameState.COMPLETE);
    }

    /**
     *
     * @param player the player fo the two
     * @return
     */
    public int getScoreCard(Player player) {
        if (player == Player.A) {
            return getPits()[getPlayerPitA()];
        }
        if (player == Player.B){
            return getPits()[getPlayerPitB()];
        }
        return 0;
    }




    public ArrayList<MancalaView> getListeners() {
        return listeners;
    }

    public void setListeners(ArrayList<MancalaView> listeners) {
        this.listeners = listeners;
    }

    public int[] getPits() {
        return pits;
    }

    public void setPits(int[] pits) {
        this.pits = pits;
    }

    public int[] getPrevPits() {
        return prevPits;
    }

    public void setPrevPits(int[] prevPits) {
        this.prevPits = prevPits;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public boolean isLastStone() {
        return isLastStone;
    }

    public void setLastStone(boolean lastStone) {
        isLastStone = lastStone;
    }

    public boolean isUndoable() {
        return isUndoable;
    }

    public void setUndoable(boolean undoable) {
        isUndoable = undoable;
    }

    public int getUndoCount1() {
        return undoCount1;
    }

    public void setUndoCount1(int undoCount1) {
        this.undoCount1 = undoCount1;
    }

    public int getUndoCount2() {
        return undoCount2;
    }

    public void setUndoCount2(int undoCount2) {
        this.undoCount2 = undoCount2;
    }

    public int maxUndo() {
        return 3;
    }

    public FormatStrategy getFormat() {
        return format;
    }

    public void setFormat(FormatStrategy format) {
        this.format = format;
    }
}