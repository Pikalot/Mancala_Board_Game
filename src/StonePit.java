import java.awt.*;
import java.awt.geom.*;

/**
 * A concrete class representing a stone pit shape that contains stones.
 *
 * @author Tuan-Anh Ho
 * @version 1.0 12/05/2024
 */
public class StonePit extends SelectableShape {
    private int x;
    private int y;
    private int width;
    private int height;
    private int numberOfStones;
    private Shape shape;
    private final int STONE_SIZE = 20;
    private Color color;
    
    /**
     * Constructor for StonePit class
     * @param x the x-coordinate of the pit
     * @param y the y-coordinate of the pit
     * @param width the width of the pit
     * @param height the height of the pit
     */
    public StonePit(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Fills in the pit and draws the stones in them
     * @param g graphics
     */
    public void fill(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(3));
        g.draw(shape);
        g.setColor(new Color(210, 180, 140));
        g.fill(shape);
        g.setStroke(new BasicStroke(2));

        // Print the number of stones in a pit
        if (getNumberOfStones() > 0) {
            g.setColor(Color.WHITE);
            Font font = new Font("Arial", Font.BOLD, 22);
            g.setFont(font);
            g.drawString(getNumberOfStones() + "", x + 25, y + 30);
        }

        // Draw stones in overlapping fashion
        int row = 1;
        for (int i = 0; i < getNumberOfStones(); i++) {
            double x;
            double y;
            if (row % 2 == 1) {
                x = this.x + 30;
                y = (this.y + 5) + 8 * row / 1.5;
            } else {
                x = this.x + 15;
                y = (this.y + 5) + 8 * row / 1.5;
            }

            Ellipse2D.Double stone = new Ellipse2D.Double(x, y + 35, getSTONE_SIZE(), getSTONE_SIZE() - 10);
            g.setColor(Color.LIGHT_GRAY);
            g.fill(stone);
            g.setColor(Color.BLACK);
            g.setStroke (new BasicStroke(2));
            g.draw(stone);
            row++;
        }
    }

    /**
     * Sets a specified color value.
     * @param c a color value
     */
    @Override
    public void setColor(Color c) {
        this.color = c;
    }

    /**
     * Returns the current color value.
     * @return the current color
     */
    @Override
    public Color getColor() {
        return color;
    }

    /**
     * Draws the pit with stones
     * @param g graphics
     */
    @Override
    public void draw (Graphics2D g)
    {
        g.draw(shape);
        fill(g);
    }

    /**
     * Translates the shape to a new location.
     * @param dx a specified x location
     * @param dy a specified y location
     */
    @Override
    public void translate(double dx, double dy) {
        x += dx;
        y += dy;
    }

    /**
     * Redraws the selected shape with a red stroke and refills it with highlighted yellow.
     * @param g a 2D shape
     */
    @Override
    public void drawSelection(Graphics2D g) {
        Color previous = g.getColor(); // Save the current color
        g.setColor(new Color(255, 255, 200, 128)); // Set translucent highlight color
        g.fill(shape); // Fill the shape for selection
        g.setColor(Color.RED); // Highlight border
        g.setStroke(new BasicStroke(3));
        g.draw(shape); // Draw border around the shape
        g.setColor(previous); // Restore the original color
    }

    /**
     * Checks if the pit contains a specified point.
     * @param p the point to be checked
     * @return true if point is in pit, false otherwise
     */
    @Override
    public boolean contains(Point2D p) {
        return shape.contains(p);
    }

    /**
     * Sets the shape of a pit to a specified shape.
     * @param s the shape to set the pit to
     */
    public void setShape(Shape s) {
        shape = s;
    }

    /**
     * Returns the shape of the pit.
     * @return the pit's shape
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * Returns the X coordinate of the pit.
     * @return the X coordinate of the pit
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the Y coordinate of the pit.
     * @return the Y coordinate of the pit
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the width of the pit.
     * @return the width of the pit
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the pit.
     * @return the height of the pit
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the number of stones in a pit.
     * @return an amount of stones
     */
    public int getNumberOfStones() {
        return numberOfStones;
    }

    /**
     * Sets the number of stones for a pit.
     * @param numberOfStones the amount of stones
     */
    public void setNumberOfStones(int numberOfStones) {
        this.numberOfStones = numberOfStones;
    }

    /**
     * Returns the size of the stones.
     * @return the fixed size of a stone
     */
    public int getSTONE_SIZE() {
        return STONE_SIZE;
    }
}