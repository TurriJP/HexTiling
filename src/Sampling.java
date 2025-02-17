import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Random;

public class Sampling {
    int n_points;
    private PApplet sketch;
    private static final Random generator = new Random();

    Sampling(int n_points, PApplet sketch) {
        this.n_points = n_points;
        this.sketch = sketch;
    }

    float minDist = 200;
    Point PoissonDiskSampling(ArrayList<Point> points) {
        int maxAttempts = 100;
        int nAttempts = 0;
        Point newPoint = new Point(sketch);
        boolean valid = false;

        while (!valid && nAttempts < maxAttempts) {
            int randomX = generator.nextInt(sketch.width);
            int randomY = generator.nextInt(sketch.height);
            newPoint = new Point(sketch, randomX, randomY, 0, 0, 0);
            valid = true;
            for(Point p : points){
                if (sketch.dist(p.x, p.y, newPoint.x, newPoint.y) < minDist) {
                    //System.out.println("Ponto: "+i+" distância: "+dist(p.x, p.y, newPoint.x, newPoint.y));
                    valid = false;
                    break;
                }
            }
            nAttempts++;

            //if (nAttempts == maxAttempts) {
            //  System.out.println("Cheguei no fim");
            //}
        }
        return newPoint;
    }

    ArrayList<Point> sample() {
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(sketch));
        for (int i = 1; i < n_points; i++) {
            points.add(PoissonDiskSampling(points)); //naive();
        }
        return points;
    }
}
