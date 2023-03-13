package org.schakalacka.java.raytracing.scene.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChunkCalculatorTest {

    @Test
    void chunkCalculatorFailsForOddNumOfChunks() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> ChunkCalculator.calculateChunks(3,10,10));
        assertEquals("Can only calculate an square number of chunks!", e.getMessage());
    }

    @Test
    void chunkCalculator1() {

        // we want to divide a 100x50 canvas into 16 chunks of equal size
        int width = 100;
        int height = 50;
        int chunks = 1;

        var calculateChunks = ChunkCalculator.calculateChunks(chunks, width, height);

        var chunk1 = calculateChunks[0];

        assertEquals(0, chunk1.xFrom());
        assertEquals(99, chunk1.xTo());
        assertEquals(0, chunk1.yFrom());
        assertEquals(49, chunk1.yTo());
    }

    @Test
    void chunkCalculator4() {

        // we want to divide a 100x50 canvas into 16 chunks of equal size
        int width = 100;
        int height = 50;
        int chunks = 4;

        var calculateChunks = ChunkCalculator.calculateChunks(chunks, width, height);

        var chunk1 = calculateChunks[0];
        var chunk2 = calculateChunks[1];
        var chunk3 = calculateChunks[2];
        var chunk4 = calculateChunks[3];

        assertEquals(0, chunk1.xFrom());
        assertEquals(49, chunk1.xTo());
        assertEquals(0, chunk1.yFrom());
        assertEquals(24, chunk1.yTo());

        assertEquals(50, chunk2.xFrom());
        assertEquals(99, chunk2.xTo());
        assertEquals(0, chunk2.yFrom());
        assertEquals(24, chunk2.yTo());

        assertEquals(0, chunk3.xFrom());
        assertEquals(49, chunk3.xTo());
        assertEquals(25, chunk3.yFrom());
        assertEquals(49, chunk3.yTo());

        assertEquals(50, chunk4.xFrom());
        assertEquals(99, chunk4.xTo());
        assertEquals(25, chunk4.yFrom());
        assertEquals(49, chunk4.yTo());
    }

    @Test
    void chunkCalculator16() {

        // we want to divide a 100x50 canvas into 16 chunks of equal size
        int width = 100;
        int height = 50;
        int chunks = 16;

        var calculateChunks = ChunkCalculator.calculateChunks(chunks, width, height);

        var chunk1 =    calculateChunks[0];
        var chunk2 =    calculateChunks[1];
        var chunk4 =    calculateChunks[3];
        var chunk5 =    calculateChunks[4];
        var chunk11 =   calculateChunks[10];
        var lastChunk = calculateChunks[15];

        assertEquals(0, chunk1.xFrom());
        assertEquals(24, chunk1.xTo());
        assertEquals(0, chunk1.yFrom());
        assertEquals(11, chunk1.yTo());

        assertEquals(25, chunk2.xFrom());
        assertEquals(49, chunk2.xTo());
        assertEquals(0, chunk2.yFrom());
        assertEquals(11, chunk2.yTo());

        assertEquals(75, chunk4.xFrom());
        assertEquals(99, chunk4.xTo());
        assertEquals(0, chunk4.yFrom());
        assertEquals(11, chunk4.yTo());

        assertEquals(0, chunk5.xFrom());
        assertEquals(24, chunk5.xTo());
        assertEquals(12, chunk5.yFrom());
        assertEquals(23, chunk5.yTo());

        assertEquals(50, chunk11.xFrom());
        assertEquals(74, chunk11.xTo());
        assertEquals(24, chunk11.yFrom());
        assertEquals(35, chunk11.yTo());

        assertEquals(75, lastChunk.xFrom());
        assertEquals(99, lastChunk.xTo());
        assertEquals(36, lastChunk.yFrom());
        assertEquals(49, lastChunk.yTo());

    }


}