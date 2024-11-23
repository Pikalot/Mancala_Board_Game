import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Scanner;

public class Tester {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        JFrame frame = new JFrame("Scene Editor");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocation(600, 300);
        int[] stones = new int[6];
        for (int i = 0; i < 6; i++) {
            stones[i] = 4;
        }
        MancalaModel model = new MancalaModel(stones);
        MComponent scene = new MComponent(model);
//        for (int i = 0; i < 6; i++) {
//            MShape pit = new StonePit((i * 60) + 10, 200, 50, 4);
////            pit.setShapes(stones);
//            scene.addShape(pit);
//        }
//        for (int i = 0; i < )

        frame.setLayout(new BorderLayout());
        frame.add(scene, BorderLayout.CENTER);
//        frame.pack();
        frame.setVisible(true);
        System.out.print("New stone: ");
        int newStone = input.nextInt();
        for (int i = 0; i < 6; i++) {
            stones[i] = newStone;
        }
        model.setStones(stones);
    }
}
