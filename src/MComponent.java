import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class MComponent extends JComponent implements MancalaView {
    private ArrayList<CompositeShape> shapes;
    private Point2D mousePoint;
    private ArrayList<MShape> stonesInPits;
    private MancalaModel model;
    private int[] stones;


    public MComponent(MancalaModel mod) {
        this.model = mod;

        shapes = new ArrayList<>();

        this.model.attach(this);
        this.addMouseListener(new MousePressedListener());
//        this.addMouseMotionListener(new MouseDraggedListener());
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < 6; i++) {
            CompositeShape pit = new StonePit((i * 60) + 10, 200, 50, model.getStones()[i]);
            System.out.println("stone again: " + model.getStones()[i]);
//            pit.setShapes(stones);
            shapes.add(pit);
        }
        for (MShape s: shapes) {
            s.draw(g2);
            if (s.isSelected()) s.drawSelection(g2);
        }
    }

    public void addShape(CompositeShape s) {
        shapes.add(s);
        repaint();
    }

    public void removeSelection() {
        for (int i = shapes.size() - 1; i >= 0; i--) {
            if (shapes.get(i).isSelected()) shapes.remove(i);
        }
        repaint();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        System.out.println("changing");
        for (int i = 0; i < shapes.size(); i++) {
            this.shapes.get(i).setNumberOfStone(model.getStones()[i]);
            System.out.println(model.getStones()[i]);
        }
        repaint();
    }

    private class MousePressedListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent event) {
            mousePoint = event.getPoint();
            for (MShape s: shapes) {
                if (s.contains(mousePoint)) s.setSelected(!s.isSelected());
            }
            repaint();
        }
    }

//    private class MouseDraggedListener extends MouseMotionAdapter {
//        @Override
//        public void mouseDragged(MouseEvent event) {
//            Point2D lastPoint = mousePoint;
//            mousePoint = event.getPoint();
//            for (MShape s: shapes) {
//                if (s.isSelected()) {
//                    double dx = mousePoint.getX() - lastPoint.getX();
//                    double dy = mousePoint.getY() - lastPoint.getY();
//                    s.translate(dx, dy);
//                }
//            }
//            repaint();
//        }
//    }

}
