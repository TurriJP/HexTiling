import processing.core.PApplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static java.lang.Math.sqrt;

public class Grid {
    PApplet sketch;
    int edgeSize;
    int halfSize;
    int columns;
    int rows;
    int emptyHexes;
    boolean processedFinalState;
    private static final Random generator = new Random();
    private int size;
    private int height;
    private int width;
    private int verticalStep;
    private int horizontalStep;
    ArrayList<ArrayList<Point>> points;
    HashMap<String, Line> edges;
    HashMap<Point, Hex> hexes;
    List<List<List<Integer>>> directionDifferences = List.of(
            // even cols
            List.of(
                    List.of(-1, -2),
                    List.of(-2, 0),
                    List.of(-1, 1),
                    List.of(1, 1),
                    List.of(2, 0),
                    List.of(1, -2)
            ),
            // odd cols
            List.of(
                    List.of(-1, -1),
                    List.of(-2, 0),
                    List.of(-1, 2),
                    List.of(1, 2),
                    List.of(2, 0),
                    List.of(1, -1)
            )
    );

    Grid(int size, PApplet sketch){
        this.sketch = sketch;
        this.size = size;
        this.width = 2 * size;
        this.height = (int)(sqrt(3) * size);
        this.horizontalStep = size;
        this.verticalStep = height / 2;
        this.columns = sketch.width / size + 1;
        this.rows = sketch.height / (height / 2) + 1;
        this.emptyHexes = 0;
        this.edges = new HashMap<>();
        this.points = generatePoints();
        this.hexes = generateHexes();
        this.processedFinalState = false;
    }

    private ArrayList<ArrayList<Point>> generatePoints() {
        ArrayList<ArrayList<Point>> points = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            points.add(new ArrayList<>());
            for (int j = 0; j < columns; j++) {
                int offset = i % 2;
                Point newPoint = new Point(
                        sketch,
                        j * horizontalStep + offset * (size/2),
                        i * verticalStep,
                        i,
                        j
                );
                points.get(i).add(newPoint);
            }
        }
        return points;
    }

    private HashMap<Point,Hex> generateHexes() {
        HashMap<Point, Hex> hexes = new HashMap<Point, Hex>();
        int i;
        int j = 0;
        for (i = 1; i < (rows - 1); i++) {
            for (j = 1; j < (columns - 1); j += 3) {
                int _j = j;
                if (i % 2 == 0) _j += 2;
                if (_j >= columns - 1) continue;
                Point p = points.get(i).get(_j);
                p.r = 250; // Make the hexagon center red
                Hex hex = new Hex(sketch, this, p);
                hexes.put(p, hex);
                emptyHexes++;
            }
        }
        return hexes;
    }

    ArrayList<Hex> neighbors(Hex hex) {
        int parity = hex.column % 3;
        if (parity != 0) parity = 1;
        ArrayList<Hex> neighbors = new ArrayList<Hex>();
        for (int i  = 0; i < 6; i++) {
            List<Integer> diff = directionDifferences.get(parity).get(i);
            int coordX = hex.column + diff.get(1);
            int coordY = hex.row + diff.get(0);
            if (
                    coordX >= 0 && coordX < columns && coordY > 0 && coordY < rows
            ) {
                neighbors.add(hexes.get(points.get(coordY).get(coordX)));
            }
        }

        return neighbors;
    }

    void colorNeighbors(Hex hex, boolean randomize) {
        int rOffset = 0;
        int gOffset = 0;
        int bOffset = 0;
        if (randomize) {
            int displacement = 50;
            rOffset = generator.nextInt(-displacement, displacement);
            gOffset = generator.nextInt(-displacement, displacement);
            bOffset = generator.nextInt(-displacement, displacement);
        }
        ArrayList<Hex> neighbors = neighbors(hex);
        for (Hex h : neighbors) {
            if (h != null && h.group == -1) {
                h.r = hex.r + rOffset;
                h.g = hex.g + gOffset;
                h.b = hex.b + bOffset;
                h.group = hex.group;
                emptyHexes--;
            }
        }
    }

    void display() {
        if (emptyHexes == 0 && !processedFinalState) {
            for (Line l : edges.values()) {
                l.processBorder();
            }
            processedFinalState = true;
        }
        sketch.pushMatrix();


        if (emptyHexes > 0) {
//            for (int i = 0; i < rows; i++) {
//                for (int j = 0; j < columns; j++) {
//                    points.get(i).get(j).display();
//                }
//            }

            for (Hex h : hexes.values()) {
                h.display();
            }
        }

        for (Line l : edges.values()) {
            l.display(emptyHexes==0);
        }

        sketch.popMatrix();


    }
}
