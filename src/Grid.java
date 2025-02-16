import processing.core.PApplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.sqrt;

public class Grid {
    PApplet sketch;
    int edgeSize;
    int halfSize;
    int columns;
    int rows;
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
                    List.of(1, 0),
                    List.of(1, -1),
                    List.of(0, -1),
                    List.of(-1, -1),
                    List.of(-1, 0),
                    List.of(0, 1)
            ),
            // odd cols
            List.of(
                    List.of(1, 1),
                    List.of(1, 0),
                    List.of(0, -1),
                    List.of(-1, 0),
                    List.of(-1, 1),
                    List.of(0, 1)
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
        this.edges = new HashMap<>();
        this.points = generatePoints();
        this.hexes = generateHexes();
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
        HashMap<Point,Hex> hexes = new HashMap<Point,Hex>();
        for (int i = 1; i < (rows - 1); i ++) {
            for (int j = 1; j < (columns - 1); j += 3) {
                int _j = j;
                if (i%2==0) _j += 2;
                if (_j >= columns - 1) continue;
                Point p = points.get(i).get(_j);
                p.r = 250;
                Hex hex = new Hex(sketch, this, p);
                hexes.put(p, hex);
            }
        }
        return hexes;
    }

    ArrayList<Hex> neighbors(Hex hex) {
        int parity = hex.column & 1;
        ArrayList<Hex> neighbors = new ArrayList<Hex>();
        for (int i  = 0; i < 6; i++) {
            List<Integer> diff = directionDifferences.get(parity).get(i);
            int coordX = hex.column + diff.get(0);
            int coordY = hex.row + diff.get(1);
            System.out.println(coordX);
            System.out.println(coordY);
            if (
                    coordX >= 0 && coordX < columns && coordY > 0 && coordY < rows
            ) {
                System.out.println(points.get(coordX).get(coordY));
                neighbors.add(hexes.get(points.get(coordX).get(coordY)));
            }
        }
        System.out.println(neighbors);
        System.out.println(neighbors.size());
        return neighbors;
    }

    void colorNeighbors(Hex hex) {
        ArrayList<Hex> neighbors = neighbors(hex);
        for (Hex h : neighbors) {
            if (h != null && h.group == -1) {
                h.r = hex.r;
                h.g = hex.g;
                h.b = hex.b;
                h.group = hex.group;
            }
        }
    }

    void display() {
        sketch.pushMatrix();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                points.get(i).get(j).display();
            }
        }

        for (Hex h : hexes.values()) {
            h.display();
        }

        for (Line l : edges.values()) {
            l.display();
        }

        sketch.popMatrix();


    }
}
