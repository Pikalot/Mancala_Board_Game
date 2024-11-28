import javax.swing.event.ChangeEvent;
import java.util.ArrayList;

/**
 * The model class in the MVC (Model-View-Controller) architecture for managing the game.
 * This class holds the number of stones on each pit and manages the game rule and states.
 *
 * @author Tuan-Anh Ho
 * @version 1.0 12/05/2024
 */
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
    public final int MAX_UNDO = 3;
    public final int MAX_PITS = 14;
    public final int PLAYER_A_PIT = 6;
    public final int PLAYER_B_PIT = 13;

    /** Constructs a Mancala Model with a specified format. */
    public MancalaModel(FormatStrategy boardFormat){
        setFormat(boardFormat);
        setListeners(new ArrayList<>());
        setPits(new int[MAX_PITS]);
        setPrevPits(new int[MAX_PITS]);
        setState(GameState.BEGIN);
        setPlayer(Player.A);
        setUndoCount1(0);
        setUndoCount2(0);
        setLastStone(false);
        setUndoable(false);
    }

    /** Notifies all registered observers of changes in the model.*/
    private void notifyListeners() {
        for (MancalaView l : getListeners()){
            l.stateChanged(new ChangeEvent(this));
        }
    }

    /**
     * Sets the initial number of stones in each pit (excluding player Mancalas)
     * and notifies listeners.
     * @param stones a specified number of stones
     */
    public void setStones(int stones) {
        for(int i = 0; i < getPits().length; i++) {
            if (!isInPlayerPit(i))
                getPits()[i] = stones;
        }
        this.notifyListeners();
    }

    /**
     * Executes a move from the selected pit, updates the game state, and notifies listeners.
     * @param pitIndex an index of the selected pit
     */
    public void move(int pitIndex) {
        // Save state
        this.save();
        setUndoable(true);
        if (!isLastStone()) resetUndoCount();

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
        this.notifyListeners();
    }

    /**
     * Reverts the last move if undo is allowed,
     * updating the game state and notifying listeners.
     */
    public void undoMove() {
        boolean flag = false;

        // If undo is not allowed, exit early
        if (!isUndoable()) {
            return;
        }

        // Handle undo logic based on the current player
        if (undoable()) {
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
            incrementUndoCount(getPlayer());
            setPits(getPrevPits().clone());
            setUndoable(false);
            this.notifyListeners(); // Notify listeners
        }
    }

    /**
     * Registers a new observer to the list.
     * @param listener a specified observer
     */
    public void attach(MancalaView listener) {
        getListeners().add(listener);
    }

    /**
     * Determines if the specified pit is playable based on the game state,
     * pit contents, and current player.
     * @param pit a specified pit
     * @return true if the pit is legal to select
     */
    public boolean playable(int pit) {
        // Check if the game is on going
        if (getState() != GameState.PLAYING) {
            return false;
        }

        // Ensure the pit is not empty
        if (getPits()[pit] == 0) {
            return false;
        }

        // Verify if it is the current player's pit
        if (getCurrentPlayer(pit) != getPlayer()) {
            return false;
        }

        return pit < MAX_PITS && pit != PLAYER_A_PIT && pit != PLAYER_B_PIT;
    }

    /**
     * Returns the scores of a player.
     * @param player a specified player
     * @return number of stones in that player's pit
     */
    public int getScoreCard(Player player) {
        if (player == Player.A) {
            return getPits()[PLAYER_A_PIT];
        }
        if (player == Player.B){
            return getPits()[PLAYER_B_PIT];
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

    public FormatStrategy getFormat() {
        return format;
    }

    public void setFormat(FormatStrategy format) {
        this.format = format;
        if (getListeners() != null) notifyListeners();
    }

    private Player getCurrentPlayer(int pit) {
        if (pit >= 0 && pit <= PLAYER_A_PIT) {
            return Player.A;
        }
        else return Player.B;
    }

    private boolean isInPlayerPit(int pit) {
        return (pit == PLAYER_A_PIT || pit == PLAYER_B_PIT);
    }

    private int getOppositePit(int pit) {
        if(pit <=12) {
            return 12 - pit;
        } else{
            return pit - 12;
        }
    }

    private boolean undoable() {
//        int undoCount = (player == Player.A) ? getUndoCount1() : getUndoCount2();
        return getUndoCount1() < MAX_UNDO && getUndoCount2() < MAX_UNDO;
    }

    private void save() {
        setPrevPits(getPits().clone());
    }

    /**
     * Switch the player's turn
     */
    private void switchPlayer() {
        setPlayer(getPlayer() == Player.A ? Player.B : Player.A);
    }

    private void moveLastStoneToMancala() {
        for (int i = 0; i < MAX_PITS; ++i) {
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
        return player == Player.A ? PLAYER_A_PIT : PLAYER_B_PIT;
    }

    private void doLastStoneRule(int pit) {
        // Last stone lands on own Mancala
        if (isOwnMancala(pit)) {
            setLastStone(true); // Free move
            return;
        }

        // Last stone lands on an empty pit
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
        int mancala = (getPlayer() == Player.A ? PLAYER_A_PIT : PLAYER_B_PIT);
        getPits()[mancala] += stones; // Pass all stone to that player pit
    }

    private boolean isWin() {
        int pAPits = 0;
        int pBPits = 0;

        for (int i = 0; i < PLAYER_A_PIT; i++) {
            pAPits += getPits()[i];
        }

        for (int i = PLAYER_A_PIT + 1; i < PLAYER_B_PIT; i++) {
            pBPits += getPits()[i];
        }

        return (pAPits == 0 || pBPits == 0);
    }

    /**
     * Resets the undo count for players.
     */
    private void resetUndoCount() {
        if (getPlayer().equals(Player.A)) {
            setUndoCount1(0);
        } else {
            setUndoCount2(0);
        }
    }

    private int dropStones(int startPit, int stones) {
        int currentPit = startPit;

        while (stones > 0) {
            currentPit = (currentPit + 1) % MAX_PITS;

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
     * Increments the undo count for the specified player.
     */
    private void incrementUndoCount(Player player) {
        if (player == Player.A) {
            setUndoCount2(getUndoCount2() + 1);
        } else {
            setUndoCount1(getUndoCount1() + 1);
        }
    }

    /**
     * Checks if the game state should continue after an undo.
     */
    private boolean isEndgameUndo() {
        return (getUndoCount1() < MAX_UNDO || getUndoCount2() < MAX_UNDO)
                && getState().equals(GameState.COMPLETE);
    }

}