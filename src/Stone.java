import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Stone extends CompositeShape {
    private int x;
    private int y;
    private int radius;

    public Stone(int x, int y, int radius) {
        Ellipse2D pit = new Ellipse2D.Double(x, y, radius , radius);
        this.setColor(Color.black);
        addShape(pit);
    }

    @Override
    public MShape createShape(int x, int y) {
        return new Stone(x, y, this.radius);
    }
}
