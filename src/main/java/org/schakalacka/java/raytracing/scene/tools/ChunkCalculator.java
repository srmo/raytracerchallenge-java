package org.schakalacka.java.raytracing.scene.tools;

import org.tinylog.Logger;

/***
 * Calculate the pixels for chunks. Used for parallel rendering.
 * ChunkCalculator only works correctly for:
 * - 1 chunk
 * - number of chunks is a perfect square
 */
public class ChunkCalculator {

    public static Chunk[] calculateChunks(int numChunks, int width, int height) {
        final Chunk[] chunks = new Chunk[numChunks];

        // TODO add check if numChunks is perfect square

        double squareRoot = Math.sqrt(numChunks);
        long tst = (long)(squareRoot + 0.5);
        if (tst*tst != numChunks && numChunks != 1) {
            throw new IllegalArgumentException("Can only calculate an square number of chunks!");
        }

        final int divider = (int) Math.sqrt(numChunks);

        // java truncates the division result for ints. So we don't need to "floor" here, as it is implicit
        final int xChunkLimit = width / divider;
        final int yChunkLimit = height / divider;

        // when x or y sizes don't fit into "divider", we need to adjust for the last chunk on each axis
        final int lastMissingX = width - (xChunkLimit * divider);
        final int lastMissingY = height - (yChunkLimit * divider);
        Logger.debug("xChunkLimit: {}, yChunkLimit: {}, lastMissingX: {}, lastMissingY: {}, divider: {}", xChunkLimit, yChunkLimit, lastMissingX, lastMissingY, divider);
        int xStart;
        int xEnd;
        int yStart;
        int yEnd;

        int rowCounter = 0;
        for (int i = 0; i < numChunks; i++) {


            if (i > 0 && i % divider == 0) {
                rowCounter++;
            }

            xStart = (i % divider) * (xChunkLimit);
            xEnd = ((i % divider) + 1) * (xChunkLimit) - 1;
            yStart = rowCounter * yChunkLimit;
            yEnd = (rowCounter * yChunkLimit) + yChunkLimit - 1;


            // every last chunk in a row needs to add additional x pixels
            if (i % divider == 3) {
                xEnd += lastMissingX;
            }

            // when we are in the last row, add the lastMissingY pixels.
            // rowCounter is zero based
            if (rowCounter == (divider - 1)) {
                yEnd += lastMissingY;
            }

            Chunk c = new Chunk(xStart, xEnd, yStart, yEnd);
            Logger.debug("Calculated chunk {} {}", i, c);
            chunks[i] = c;
        }

        return chunks;
    }
}
