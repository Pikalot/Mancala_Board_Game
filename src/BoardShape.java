import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class BoardShape extends CompositeShape {
    private int x;
    private int y;
    private int width;

    public BoardShape(int x, int y, int width) {
//        stones = new ArrayList<>();
//        numberOfStone = stoneNumber;
        RoundRectangle2D rec = new RoundRectangle2D.Double(x, y, width, width/2, 5, 5);
        ArrayList<MShape> stones = getShapes();

        addShape(rec);
    }
    @Override
    public MShape createShape(int x, int y) {
        return new BoardShape(x, y, this.width);
    }
}
