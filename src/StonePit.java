import java.awt.*;
import java.awt.geom.*;

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
     * Constructor for PitShape class
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
     * Fills in the pit and draws the marbles in them
     * @param g graphics
     */
    public void fill(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(10));
        g.draw(shape);
        g.setStroke(new BasicStroke(3));

        // Print the number of stones in a pit
        if (getNumberOfStones() > 0) {
            g.setColor(Color.WHITE);
            Font font = new Font("Arial", Font.BOLD, 20);
            g.setFont(font);
            g.drawString(getNumberOfStones() + "", x + 30, y + 20);
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

            Ellipse2D.Double stone = new Ellipse2D.Double(x, y + 25, getSTONE_SIZE(), getSTONE_SIZE() - 10);
            g.setColor(Color.LIGHT_GRAY);
            g.fill(stone);
            g.setColor(Color.BLACK);
            g.setStroke (new BasicStroke(2));
            g.draw(stone);
            row++;
        }
    }

    @Override
    public void setColor(Color c) {
        this.color = c;
    }

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

    @Override
    public void translate(double dx, double dy) {
        x += dx;
        y += dy;
    }

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
     * Checks if the pit contains a specified point
     * @param p the point to be checked
     * @return true if point is in pit, false otherwise
     */
    @Override
    public boolean contains(Point2D p) {
        return shape.contains(p);
    }

    /**
     * Sets the shape of a pit to a specified shape
     * @param s the shape to set the pit to
     */
    public void setShape(Shape s) {
        shape = s;
    }

    /**
     * Gets the shape of the pit
     * @return the pit's shape
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * Gets the X coordinate of the pit
     * @return the X coordinate of the pit
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the Y coordinate of the pit
     * @return the Y coordinate of the pit
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the width of the pit
     * @return the width of the pit
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the pit
     * @return the height of the pit
     */
    public int getHeight() {
        return height;
    }

    public int getNumberOfStones() {
        return numberOfStones;
    }

    public void setNumberOfStones(int numberOfStones) {
        this.numberOfStones = numberOfStones;
    }

    public int getSTONE_SIZE() {
        return STONE_SIZE;
    }
}