package org.schakalacka.java.raytracing;

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

    public void write(int x, int y, Color val) {
        this.pixels[x][y] = val;
    }

    public Color read(int x, int y) {
        return this.pixels[x][y];
    }

}
