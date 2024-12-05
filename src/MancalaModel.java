import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;

/**
 * The model class in the MVC (Model-View-Controller) architecture for managing the game.
 * This class holds the number of stones on each pit and manages the game rule and states.
 *
 * @author Tuan-Anh Ho
 * @version 1.2 12/05/2024
 */
public class MancalaModel {
    private ArrayList<ChangeListener> listeners;
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

    /** Constructs a default Mancala Model. */
    public MancalaModel(){
        setFormat(new OakBoardFormat()); // A default format for the board
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
        for (ChangeListener l : getListeners()){
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
            else getPits()[i] = 0; // Mancala Pit
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

        // Perform the undo operation
        if (flag) {
            // Update game state if required
            if (isEndgameUndo()) state = GameState.PLAYING;
            incrementUndoCount(getPlayer());
            setPits(getPrevPits().clone());
            setUndoable(false);
            this.notifyListeners(); // Notify listeners
        }
    }

    /**
     * Notifies all observers to start a new game.
     */
    public void startNewGame() {
        setState(GameState.PLAYING);
        notifyListeners();
    }

    /**
     * Registers a new observer to the list.
     * @param listener a specified observer
     */
    public void attach(ChangeListener listener) {
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

    /**
     * Returns a list of observers of this model.
     * @return the list of observers
     */
    public ArrayList<ChangeListener> getListeners() {
        return listeners;
    }

    /**
     * Sets a new list of observers for this model.
     * @param listeners a specified list
     */
    public void setListeners(ArrayList<ChangeListener> listeners) {
        this.listeners = listeners;
    }

    /**
     * Returns an array of stones in all pits.
     * @return an array of stones number
     */
    public int[] getPits() {
        return pits;
    }

    /**
     * Sets an array of stones for all pits.
     * @param pits a specified array of number of stone
     */
    public void setPits(int[] pits) {
        this.pits = pits;
    }

    /**
     * Returns an array of stones in all pits from the game's previous state.
     * @return an array of stones in previous state
     */
    public int[] getPrevPits() {
        return prevPits;
    }

    /**
     * Sets an array of stones for all pits of previous state.
     * @param prevPits a specified array of number of stones
     */
    public void setPrevPits(int[] prevPits) {
        this.prevPits = prevPits;
    }

    /**
     * Returns the current player stored in this model.
     * @return a current player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets current player for this model.
     * @param player a specified player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Returns the state of this game.
     * @return a game state
     */
    public GameState getState() {
        return state;
    }

    /**
     * Sets the game state for this game.
     * @param state a specified state
     */
    public void setState(GameState state) {
        this.state = state;
    }

    /**
     * Checks if the stone is dropped at a player's pit.
     * @return true if stone is in a player's pit
     */
    public boolean isLastStone() {
        return isLastStone;
    }

    /**
     * Sets the result of the query if the last stone is in a player's pit.
     */
    public void setLastStone(boolean lastStone) {
        isLastStone = lastStone;
    }

    /**
     * Returns the result of a query if the game is undoable.
     * @return true if the move is undoable
     */
    public boolean isUndoable() {
        return isUndoable;
    }

    /**
     * Sets the result of the query if the move is undoable.
     */
    public void setUndoable(boolean undoable) {
        isUndoable = undoable;
    }

    /**
     * Returns the undo count of player A.
     * @return an undo count
     */
    public int getUndoCount1() {
        return undoCount1;
    }

    /**
     * Sets the new undo count value for player A.
     * @param undoCount1 a value of undo count
     */
    public void setUndoCount1(int undoCount1) {
        this.undoCount1 = undoCount1;
    }

    /**
     * Returns the undo count of player B.
     * @return an undo count
     */
    public int getUndoCount2() {
        return undoCount2;
    }

    /**
     * Sets the new undo count value for player B.
     * @param undoCount2 a value of undo count
     */
    public void setUndoCount2(int undoCount2) {
        this.undoCount2 = undoCount2;
    }

    /**
     * Returns the format strategy of this model.
     * @return a format style
     */
    public FormatStrategy getFormat() {
        return format;
    }

    /**
     * Sets a format strategy for this model.
     * @param format a format style
     */
    public void setFormat(FormatStrategy format) {
        this.format = format;
        if (getListeners() != null) notifyListeners();
    }

    /**
     * Returns the current player based on the pit index.
     * @param pit a pit index
     * @return a player owns that pit
     */
    private Player getCurrentPlayer(int pit) {
        if (pit >= 0 && pit <= PLAYER_A_PIT) {
            return Player.A;
        }
        else return Player.B;
    }

    /**
     * Checks if a pit is a Mancala pit based on a pit index.
     * @param pit a pit index
     * @return true if the pit is a Mancala pit.
     */
    private boolean isInPlayerPit(int pit) {
        return (pit == PLAYER_A_PIT || pit == PLAYER_B_PIT);
    }

    /**
     * Returns the index of the pit opposite to the specified pit index.
     * @param pit a specified pit index
     * @return the index of the opposite pit
     */
    private int getOppositePit(int pit) {
        if(pit <=12) {
            return 12 - pit;
        } else{
            return pit - 12;
        }
    }

    /**
     * Checks if players can undo a move based on their undo counts.
     * @return true if the count is less than 3.
     */
    private boolean undoable() {
        return getUndoCount1() < MAX_UNDO && getUndoCount2() < MAX_UNDO;
    }

    /**
     * Saves the state of the board.
     */
    private void save() {
        setPrevPits(getPits().clone());
    }

    /**
     * Switch the player's turn
     */
    private void switchPlayer() {
        setPlayer(getPlayer() == Player.A ? Player.B : Player.A);
    }

    /**
     * Moves all remaining stones to respective player's pits when game ends.
     */
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

    /**
     * Returns the Mancala pit index of a player's.
     * @param player a specified player
     * @return the index of the player's Mancala
     */
    private int getPlayerMancala(Player player) {
        return player == Player.A ? PLAYER_A_PIT : PLAYER_B_PIT;
    }

    /**
     * Applies the last stone rule, granting a free move if it lands in the player's Mancala,
     * stealing stones if it lands in an empty pit, or switching turns otherwise.
     * @param pit a specified pit index
     */
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

    /**
     * Checks if the specified pit index is the player's own Mancala.
     * @param pit the index of the pit to check
     * @return true if the pit is the player's Mancala, false otherwise
     */
    private boolean isOwnMancala(int pit) {
        return getCurrentPlayer(pit) == getPlayer() && isInPlayerPit(pit);
    }

    /**
     * Checks if the specified pit is the player's own empty pit
     * and the opposite pit has stones.
     * @param pit the index of the pit to check
     * @return true if the pit is the player's own, is empty, and the opposite pit contains stones
     */
    private boolean isOwnEmptyPit(int pit) {
        return getCurrentPlayer(pit) == getPlayer() &&
                getPits()[pit] == 1 &&
                getPits()[getOppositePit(pit)] > 0;
    }

    /**
     * Transfers all stones from the specified pit and its opposite pit
     * to the player's Mancala, leaving both pits empty.
     *
     * @param currentPit the index of the current pit whose stones will be stolen
     */
    private void stealStones(int currentPit) {
        int stones = getPits()[currentPit] + getPits()[getOppositePit(currentPit)];
        getPits()[currentPit] = 0;
        getPits()[getOppositePit(currentPit)] = 0;
        // Find the player pit
        int mancala = (getPlayer() == Player.A ? PLAYER_A_PIT : PLAYER_B_PIT);
        getPits()[mancala] += stones; // Pass all stone to that player pit
    }

    /**
     * Checks if the game has ended by determining if all pits
     * on one player's side are empty.
     *
     * @return true if either Player A's or Player B's pits are all empty
     */
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

    /**
     * Distributes stones starting from the specified pit, skipping opponent's Mancala pits,
     * and returns the index of the last pit where a stone is placed.
     * @param startPit the index of the pit to start dropping stones from
     * @param stones the number of stones to distribute
     * @return the index of the last pit where a stone is placed
     */
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