import javax.swing.event.ChangeEvent;
import java.util.ArrayList;

public class MancalaModel {
    private ArrayList<MancalaView> listeners;
    private int[] stones;

    public MancalaModel(int[] stones) {
        this.setListeners(new ArrayList<>());
        this.stones = stones;
    }


    public ArrayList<MancalaView> getListeners() {
        return listeners;
    }

    public void setListeners(ArrayList<MancalaView> listeners) {
        this.listeners = listeners;
    }

    public void attach(MancalaView listener) {
        this.listeners.add(listener);
    }

    public int[] getStones() {
        return stones;
    }

    public void setStones(int[] stones) {
        this.stones = stones;
        notifyListeners();
    }

    public void updateStone(int index, int value){
        stones[index] = value;
        notifyListeners();
    }

    public void notifyListeners() {
        for (MancalaView l: listeners) {
            l.stateChanged(new ChangeEvent(this));
        }
    }
}
