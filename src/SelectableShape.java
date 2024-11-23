import java.awt.*;

public abstract class SelectableShape implements MShape {
    private boolean selected;

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean s) {
        selected = s;
    }

    // Template method
    @Override
    public void drawSelection(Graphics2D g) {
        Color previous = getColor();
        setColor(new Color(255, 255, 200, 128));
        draw(g);
        translate(1, 1);
        draw(g);
        translate(-1, -1);
        setColor(previous);
    }
}
