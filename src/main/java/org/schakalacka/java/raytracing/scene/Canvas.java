package org.schakalacka.java.raytracing.scene;

public class Canvas {

    private final int width;
    private final int height;

    private final Color[][] pixels;

    public Canvas(int width, int height) {
        this(width, height, Color.BLACK);
    }

    public Canvas(int width, int height, Color initColor) {
        this.width = width;
        this.height = height;
        this.pixels = new Color[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.pixels[x][y] = initColor;
            }
        }
    }


    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    /***
     * Be aware that the canvas has coordinate 0,0 at the top-left corner.
     * Any point that would go outside the defined width/height will be smushed at 0 or widht/height.
     * I.e. if you pass in a "x" value of 233 when width is only 200, the pixel will be painted at x=200
     *
     */
    public void write(int x, int y, Color val) {
        x = Math.max(x, 0);
        x = Math.min(x, (this.width - 1));

        y = Math.max(y, 0);
        y = Math.min(y, (this.height -1));
        this.pixels[x][y] = val;
    }

    public Color read(int x, int y) {
        return this.pixels[x][y];
    }

}
