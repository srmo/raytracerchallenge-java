package org.schakalacka.java.raytracing;

public class RenderClock {

    private Canvas canvas;

    public static void main(String[] args) {
        RenderClock s = new RenderClock();
        s.canvas = new Canvas(700, 700);

        int clockRadius = 200;
        // start at twelve'o'clock. AND USE A NORMALIZED Point! We will scale this thing later when painting the pixel.
        // following the idea of the book here, to project the clock on the Z axis. I.e. we will automagically rotate around Y axis
        // and therefor get clockwise rotation (remember, our matrix rotation impl uses left-hand-rule)
        Tuple hourHand = Tuple.point(0,0,1);

        // TODO: use projection on the Y axis
        var radiansPerHour = Math.PI / 6;
        var rotationMatrix = Matrix.rotationY(radiansPerHour);


        // okay, well, I'm still getting a headache! Paint the z coordinate on the y- axis. See above
        for (int hour = 0; hour < 12; hour++) {
            s.canvas.write((int) (hourHand.x()*clockRadius)+350, (int) (hourHand.z()*clockRadius)+350, Color.WHITE);
            hourHand = rotationMatrix.mulT(hourHand);
        }



        long start = System.currentTimeMillis();
        PPMExporter.export(s.canvas, "clockrender.ppm", 255);
        long end = System.currentTimeMillis();
        System.out.println("Export took " + (end - start) + " ms");
    }


}
