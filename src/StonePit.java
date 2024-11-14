import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public class StonePit implements CompositeShape {
    private int x;
    private int y;
    private int radius;
    private boolean selectable = false;
    private GeneralPath path;

    public StonePit(int x, int y, int radius) {
        path = new GeneralPath();

        // Define the main shape for StonePit, e.g., a circle
        Ellipse2D pit = new Ellipse2D.Double(x, y, radius , radius/2);
        path.append(pit, false);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.GRAY);
        g2.fill(path);
        g2.setColor(Color.BLACK);
        g2.draw(path);
    }

    @Override
    public CompositeShape createShape(int newX, int newY) {
        return new StonePit(newX, newY, this.radius);
    }

  /*  @Override
    public boolean isSelected() {
        return contains()
    }*/

    @Override
    public boolean contains(int x, int y) {
        return path.contains(x, y);
    }

    @Override
    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

}

