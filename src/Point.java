import processing.core.PApplet;

import java.util.Random;

public class Point implements Comparable<Point> {
    private PApplet sketch;
    int x, y, row, column;
    float r, g, b;
    private static final Random generator = new Random();

    Point(PApplet sketch) {
        this(sketch, generator.nextInt(sketch.width), generator.nextInt(sketch.height), 0, 0, 0);
    }

    Point(PApplet sketch, int tx, int ty) {
        this(sketch, tx, ty, 100, 100, 100);
    }

    Point(PApplet sketch, int tx, int ty, float tr, float tg, float tb) {
        this.sketch = sketch;
        this.x = tx;
        this.y = ty;
        this.r = tr;
        this.g = tg;
        this.b = tb;
    }

    Point(PApplet sketch, int tx, int ty, int row, int column) {
        this.sketch = sketch;
        this.x = tx;
        this.y = ty;
        this.row = row;
        this.column = column;
    }

    public int compareTo(Point other) {
        return Double.compare(this.x, other.x);
    }

    void display() {
        sketch.pushMatrix();
        sketch.translate(x, y);
        sketch.fill(r, g, b);
        sketch.ellipse(0, 0, 10, 10);
        sketch.popMatrix();
    }
}