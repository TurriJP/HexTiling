import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Random;

public class Main extends PApplet {

    private ArrayList<Point> points;
    public Grid grid;
    private int[][] groups;
    int nGroups;
    private static final Random generator = new Random();
    int time;
    int timeStep;
    boolean autoPopulate = true;
    boolean noWait = true;
    int maxGroups = 15;

    public void settings(){
        size(1200, 800);
        Sampling s = new Sampling(maxGroups, this);
        points = s.sample();
        grid = new Grid(8, this);
        groups = new int[maxGroups][3];
        nGroups = 0;
        time = millis();
        timeStep = 100;

        if (autoPopulate) {
            for (Point p : points) {
                for (Hex h : grid.hexes.values()) {
                    if (h.internalPoint(p) != 0) {
                        groups[nGroups] = new int[3];
                        for (int i = 0; i < 3; i++) {
                            groups[nGroups][i] = generator.nextInt(255);
                        }
                        h.r = groups[nGroups][0];
                        h.g = groups[nGroups][1];
                        h.b = groups[nGroups][2];
                        h.group = nGroups;
                        nGroups++;
                        grid.emptyHexes--;
                    }
                }
            }
        }
    }

    public void draw() {
        background(200, 200, 200);
        grid.display();
        if ((noWait || (millis() > time + timeStep)) && grid.emptyHexes > 0) {
            System.out.println("Empty hexes: "+grid.emptyHexes);
            for (Hex h : grid.hexes.values()) {
                if (h.group != -1) {
                    grid.colorNeighbors(h, false);
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
                if(nGroups < maxGroups ) {
                    groups[nGroups] = new int[3];
                    for (int i = 0; i < 3; i++) {
                        groups[nGroups][i] = generator.nextInt(255);
                    }
                    h.r = groups[nGroups][0];
                    h.g = groups[nGroups][1];
                    h.b = groups[nGroups][2];
                    h.group = nGroups;
                    nGroups++;
                    grid.emptyHexes--;
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