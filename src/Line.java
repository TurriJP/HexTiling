import processing.core.PApplet;

import java.util.ArrayList;

public class Line{
    PApplet sketch;
    Point a;
    Point b;
    boolean show;
    ArrayList<Hex> neighboringHex;

    Line(Point ta, Point tb, PApplet sketch){
        this.a = ta;
        this.b = tb;
        this.sketch = sketch;
        this.show = true;
        this.neighboringHex = new ArrayList<>();
    }

    void display() {
        if(show) {
            sketch.pushMatrix();
            sketch.fill(0, 0, 0);
            createLine();
            sketch.popMatrix();
        }
    }

    void processBorder() {
        if (neighboringHex.size() == 2 && neighboringHex.get(0).group == neighboringHex.get(1).group) {
            show = false;
        }
    }

    void createLine() {
        straightLine();
//        jaggedLine();
    }

    void straightLine() {
        sketch.line(a.x, a.y, b.x, b.y);
    }

    void jaggedLine() {
        int segments = 100;
        float noiseScale = 0.1F;

        float prevX = a.x;
        float prevY = a.y;

        for (int i = 1; i <= segments; i++) {
            float t = (float) i / segments;  // Interpolation factor

            float x = PApplet.lerp(a.x, b.x, t);
            float y = PApplet.lerp(a.y, b.y, t);

            float noiseValue = (float) (sketch.noise(x * noiseScale, y * noiseScale) - 0.5);
            float offset = noiseValue * 20; // Scale the offset

            if (i > 0) {
                sketch.line(prevX, prevY, x, y + offset);
            }

            prevX = x;
            prevY = y + offset;
        }
    }
}