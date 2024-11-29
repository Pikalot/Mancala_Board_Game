/**
 * An abstract class representing a shape that can be selected.
 * Implements the MShape interface and provides functionality
 * to check and modify the selection state of the shape.
 *
 * @author Tuan-Anh
 * @version 1.0 12/05/2024
 */
public abstract class SelectableShape implements MShape {
    private boolean selected;

    /**
     * Checks if the shape is selected.
     * @return true if the shape is selected
     */
    @Override
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets the selection state of the shape.
     * @param s a boolean value of selection state
     */
    @Override
    public void setSelected(boolean s) {
        selected = s;
    }
}
