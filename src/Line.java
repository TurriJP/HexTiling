import processing.core.PApplet;

public class Line{
    PApplet sketch;
    Point a;
    Point b;
    boolean show;

    Line(Point ta, Point tb, PApplet sketch){
        this.a = ta;
        this.b = tb;
        this.sketch = sketch;
        this.show = true;
    }

    void display() {
        if(show) {
            sketch.pushMatrix();
            sketch.fill(0, 0, 0);
            createLine();
            sketch.popMatrix();
        }
    }

    void createLine() {
        sketch.line(a.x, a.y, b.x, b.y);
    }
}