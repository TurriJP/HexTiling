import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Random;

public class Main extends PApplet {

    private ArrayList<Point> points;
    private Grid grid;
    private int[][] groups;
    int nGroups;
    private static final Random generator = new Random();
    int time;

    public void settings(){
        size(800, 800);
        Sampling s = new Sampling(15, this);
        points = s.sample();
        grid = new Grid(32, this);
        groups = new int[10][3];
        nGroups = 0;
        time = millis();
    }

    public void draw() {
        background(200, 200, 200);
//        for(Point p : points) {
//            p.display();
//        }
        grid.display();
        if (millis() > time + 2000) {
            System.out.println("ESTOU NO IF0");
            for (Hex h : grid.hexes.values()) {
                if (h.group != -1) {
                    grid.colorNeighbors(h);
                }
            }
            time = millis();
        }
    }

    public void mouseClicked() {
        Point clickedPoint = new Point(this, mouseX, mouseY);
//        points.add(clickedPoint);
        for (Hex h : grid.hexes.values()) {
            if (h.internalPoint(clickedPoint) != 0) {
                if(nGroups < 9 ) {
                    groups[nGroups] = new int[3];
                    for (int i = 0; i < 3; i++) {
                        groups[nGroups][i] = generator.nextInt(255);
                    }
                    h.r = groups[nGroups][0];
                    h.g = groups[nGroups][1];
                    h.b = groups[nGroups][2];
                    h.group = nGroups;
                    nGroups++;
                }
            }
        }

    }

    public static void main(String[] args) {
        String[] processingArgs = {"MySketch"};
        Main mySketch = new Main();
        PApplet.runSketch(processingArgs, mySketch);
    }
}