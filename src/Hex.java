import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class Hex {
    PApplet sketch;
    Point[] vertexes;
    Line[] edges;
    int group, r, g, b;
    int row, column;
    CubicCoord cubeCoord;
    boolean top, bottom, left, right;

    Hex(PApplet sketch, Grid grid, Point center) {
        this.sketch = sketch;
        this.group = -1;
        this.r = 255;
        this.g = 255;
        this.b = 255;

        int centerX = center.row;
        int centerY = center.column;
        this.row = center.row;
        this.column = center.column;

        this.cubeCoord = new CubicCoord(centerX, centerY, sketch.width, sketch.height);

        top = row == 0;
        left = column == 0;
        right = row == grid.rows - 1;
        bottom = column == grid.columns - 1;

        // Processar vértices
        vertexes = new Point[6];
        if (centerX % 2 == 1) {
            vertexes[0] = grid.points.get(centerX - 1).get(centerY);
            vertexes[1] = grid.points.get(centerX - 1).get(centerY + 1);
            vertexes[2] = grid.points.get(centerX).get(centerY + 1);
            vertexes[3] = grid.points.get(centerX + 1).get(centerY + 1);
            vertexes[4] = grid.points.get(centerX + 1).get(centerY);
        }
        else {
            vertexes[0] = grid.points.get(centerX - 1).get(centerY - 1);
            vertexes[1] = grid.points.get(centerX - 1).get(centerY);
            vertexes[2] = grid.points.get(centerX).get(centerY + 1);
            vertexes[3] = grid.points.get(centerX + 1).get(centerY);
            vertexes[4] = grid.points.get(centerX + 1).get(centerY - 1);
        }
        vertexes[5] = grid.points.get(centerX).get(centerY - 1);

        edges = new Line[6];
        for (int i = 0; i < 6; i++) {
            Line l = new Line(vertexes[i], vertexes[(i+1)%6], sketch);
            edges[i] = l;
            grid.edges.putIfAbsent(pointsAsString(l.a, l.b), l);
        }
    }

    String pointsAsString(Point a, Point b) {
        String result;
        if (a.x > b.x) {
            result = "" + a.row + a.column + b.row + b.column;
        }
        else if (a.x == b.x) {
            if (a.y > b.y) {
                result = "" + a.row + a.column + b.row + b.column;
            }
            else {
                result = "" + b.row + b.column + a.row + a.column;
            }
        }
        else {
            result = "" + b.row + b.column + a.row + a.column;
        }
        return result;
    }

    int isLeft( Point P0, Point P1, Point P2 )
    {
        return ( (P1.x - P0.x) * (P2.y - P0.y)
                - (P2.x -  P0.x) * (P1.y - P0.y) );
    }

    int internalPoint(Point P ) {
        // https://web.archive.org/web/20130126163405/http://geomalgorithms.com/a03-_inclusion.html
        int wn = 0;    // the  winding number counter

        // loop through all edges of the polygon
        for (int i=0; i<6; i++) {   // edge from V[i] to  V[i+1]
            if (vertexes[i].y <= P.y) {          // start y <= P.y
                if (vertexes[(i+1) % 6].y  > P.y)      // an upward crossing
                    if (isLeft( vertexes[i], vertexes[(i+1) % 6], P) > 0)  // P left of  edge
                        ++wn;            // have  a valid up intersect
            }
            else {                        // start y > P.y (no test needed)
                if (vertexes[(i+1) % 6].y  <= P.y)     // a downward crossing
                    if (isLeft( vertexes[i], vertexes[(i+1) % 6], P) < 0)  // P right of  edge
                        --wn;            // have  a valid down intersect
            }
        }
        return wn;
    }


//    List<Hex> neighbors() {
//
//        // First element is the top hexagon,
//        // the rest follow clockwise
//
//        OffsetCoord topHex = null;
//        OffsetCoord topLeftHex = null;
//        OffsetCoord topRightHex = null;
//        OffsetCoord bottomHex = null;
//        OffsetCoord bottomLeftHex = null;
//        OffsetCoord bottomRightHex = null;
//        if (!top) {
//            topHex = cube_to_oddq(this);
//
//        }
//        if (!left) {
//
//        }
//
//        List<OffsetCoord> neighbors = List.of(topHex, null, null, null, null, null);
//        return neighbors;
//    }

    void display() {
        if (group >= 0) {
            sketch.pushMatrix();
            sketch.fill(r, g, b);
            sketch.beginShape();
            for (int i = 0; i < 6; i++) {
                sketch.vertex(vertexes[i].x, vertexes[i].y);
            }
            sketch.endShape();
            sketch.popMatrix();
        }

    }

    OffsetCoord cube_to_oddq(Hex hex){
    int col = hex.cubeCoord.q;
    int row = hex.r + (hex.cubeCoord.q - (hex.cubeCoord.q&1)) / 2;
    return new OffsetCoord(col, row);
    }

}

class CubicCoord{
    int q, r, s;
    CubicCoord(int gridX, int gridY, int maxWidth, int maxHeight) {
        this.q = gridX;
        this.r = gridY - (gridX - (gridX&1)) / 2;
        this.s = -q -r;

    }

}

class OffsetCoord{
    int x, y;
    OffsetCoord(int x, int y) {
        this.x = x;
        this.y = y;
    }
}