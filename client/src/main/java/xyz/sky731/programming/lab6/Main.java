package xyz.sky731.programming.lab6;

import java.awt.*;
import java.util.Random;

import javax.swing.JFrame;

import xyz.sky731.programming.lab7.Coordinates;

import cartesian.coordinate.CCPoint;
import cartesian.coordinate.CCPolygon;
import cartesian.coordinate.CCSystem;
import cartesian.coordinate.CCLine;

public class Main extends JFrame {
    private static final long serialVersionUID = 1L;

    Main() {
        super("Viewer");
        setTitle("Viewer");

        setVisible(true);
        setSize(600, 600);
        setLocationRelativeTo(null);
        CCSystem s = new CCSystem(-10.0, -10.0, 10.0, 10.0);
        add(s);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


//        double[] x = new double[]{-1,2,0};
//        double[] y = new double[]{-1,-2,3};
//        CCPolygon ccp = new CCPolygon(x, y, Color.red, Color.red, new BasicStroke(1f));
//        s.add(ccp);
//
//
//
//        s.add(new CCLine(1.0, 0.0, Color.blue));
//        s.add(new CCLine(-1.0, 10.0, Color.magenta));
//        s.add(new CCLine(1.0, 5.0, Color.red));
//        s.add(new CCLine(-1.0, 5.0, Color.cyan));
//        s.add(new CCLine(-1.0, 15.0, Color.yellow));
//        s.add(new CCLine(1.0, -5.0, Color.green));
//        s.add(new CCLine(1.0, 0.0, 5.0, Color.orange));
//        s.add(new CCLine(0.0, 1.0, 5.0, Color.pink));
//        s.add(new CCPoint(4, 5));
//
//        int corx = 5;
//        int cory = 7;
//        int radius = 2;
//
//        Coordinates coordinates = Coordinates.Companion.caclCircleCoords(corx, cory, radius);
//        CCPolygon ccp1 = new CCPolygon(coordinates.getX(), coordinates.getY(), Color.blue, Color.blue, new BasicStroke(1f));
//        s.add(ccp1);

    }
    public static void main(String[] args) {
        new Main();
    }



}