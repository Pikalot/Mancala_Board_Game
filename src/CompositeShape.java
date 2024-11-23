import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public abstract class CompositeShape extends SelectableShape {
    private Path2D path;
    private Color color;
    private ArrayList<MShape> shapes;
    private int numberOfStone;

    public CompositeShape() {
        path = new Path2D.Double();
        color = Color.lightGray;
        setShapes(new ArrayList<>());
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fill(path);
        g.setColor(Color.black);
        g.draw(path);
    }

    @Override
    public void translate(double dx, double dy) {
        path.transform(AffineTransform.getTranslateInstance(dx, dy));
    }

    @Override
    public boolean contains(Point2D p) {
        return path.contains(p);
    }

    @Override
    public void setColor(Color c) {
        color = c;
    }

    public Color getColor() {
        return color;
    }

    protected void addShape(Shape s) {
        path.append(s, false);
    }

    public void addComponent(MShape shape) {
        getShapes().add(shape);
    }

    public void removeComponent(MShape shape) {
        shapes.remove(shape);
    }


    public ArrayList<MShape> getShapes() {
        return shapes;
    }

    protected void setShapes(ArrayList<MShape> shapes) {
        this.shapes = shapes;
    }

    public int getNumberOfStone() {
        return numberOfStone;
    }

    public void setNumberOfStone(int numberOfStone) {
        this.numberOfStone = numberOfStone;
    }
}
