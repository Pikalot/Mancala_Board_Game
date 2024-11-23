import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class StonePit extends CompositeShape {
    private int x;
    private int y;
    private int radius;
//    private int numberOfStone;
    private ArrayList<Stone> stones;

    public StonePit(int x, int y, int radius, int stoneNumber) {
        stones = new ArrayList<>();
//        int numberOfStone = getNumberOfStone();
        Ellipse2D pit = new Ellipse2D.Double(x, y, radius , radius/2);
        ArrayList<MShape> stones = getShapes();
        int row = 0;
        for (int i = 0; i < stoneNumber; i++) {
            int sX = x + ((i % 2) * (stoneNumber + 10)) + radius/4;
            int sY = y + (row * (stoneNumber + 5)) + radius/16;
            stones.add(new Stone(sX, sY, 10));
            if ((i + 1) % 2 == 0) {
                row++;
            }
//            stones.add(new Stone(x +i * 15, y + 15, 10));
        }
        addShape(pit);
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g); // Draw the pit itself (the Ellipse2D)

        // Draw all the stones inside the pit
        for (MShape stone : getShapes()) {
            stone.draw(g);
        }
    }

    @Override
    public MShape createShape(int x, int y) {
        return new StonePit(x, y, this.radius, this.getNumberOfStone());
    }

//    public void setNumberOfStone(int val) {
//        numberOfStone = val;
//    }

//    @Override
//    public void update() {
////        numberOfStone =
//    }
}

