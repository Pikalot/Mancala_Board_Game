import java.awt.*;
import java.awt.geom.Point2D;

/**
 * An interface represents a Mancala shape.
 *
 * @author Tuan-Anh Ho
 * @version 1.0 12/05/2024
 */
public interface MShape {

    /**
     * Returns true if the shape is selected.
     * @return true if selected, false if not selected
     */
    boolean isSelected();

    /**
     * Sets the boolean selected value for a shape.
     * @param s a boolean value
     */
    void setSelected(boolean s);

    /**
     * Draws a shape.
     * @param g a 2D shape
     */
    void draw(Graphics2D g);

    /**
     * Redraws a selected shape.
     * @param g a 2D shape
     */
    void drawSelection(Graphics2D g);

    /**
     * Sets a new location for the shape.
     * @param dx a specified x location
     * @param dy a specified y location
     */
    void translate(double dx, double dy);

    /**
     * Checks if the shape contains a point.
     * @param p a 2D point
     * @return true if the point is inside the shape
     */
    boolean contains(Point2D p);

    /**
     * Sets the color for the shape.
     * @param c a color value
     */
    void setColor(Color c);

    /**
     * Returns the color of the shape.
     * @return the color value
     */
    Color getColor();
}