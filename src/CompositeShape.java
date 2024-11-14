import java.awt.*;

public interface CompositeShape {
    void draw(Graphics2D g2);
    CompositeShape createShape(int x, int y); // Considering if it is necessary
//    boolean isSelected();
    void setSelectable(boolean selectable);
    boolean contains(int x, int y);
}
