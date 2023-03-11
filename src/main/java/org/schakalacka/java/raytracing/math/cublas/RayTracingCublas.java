package org.schakalacka.java.raytracing.math.cublas;

import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.jcublas.JCublas;
import jcuda.jcublas.JCublas2;
import jcuda.jcublas.cublasHandle;
import org.schakalacka.java.raytracing.math.CublasMatrix;
import org.schakalacka.java.raytracing.math.Tuple;

import static jcuda.jcublas.JCublas2.*;
import static jcuda.jcublas.cublasOperation.CUBLAS_OP_N;
import static jcuda.runtime.JCuda.cudaFree;
import static jcuda.runtime.JCuda.cudaMalloc;

public class RayTracingCublas {

    /* a function to setup a jcublas2 context */
    public static void setupContext() {
    }

    /* a function to destroy a jcublas2 context */
    public static void destroyContext() {
    }

    public static Tuple mulT(float[] matrix, Tuple tuple, int size) {
        final float[] input = new float[4];
        final float[] output = new float[4];
        final Pointer inputPointer = new Pointer();
        final Pointer outputPointer = new Pointer();

        // Fill the input and output vector
        for (int i = 0; i < size; i++) {
            input[i] = (float) tuple.get(i);
            output[i] = 0;
        }

        Pointer matrixPointer = new Pointer();

        // Allocate device memory for the input and output
        JCublas.cublasAlloc(size, Sizeof.FLOAT*4, inputPointer);
        JCublas.cublasAlloc(size, Sizeof.FLOAT*4, outputPointer);
        cudaMalloc(matrixPointer, (long) size * size * Sizeof.FLOAT);

        // Initialize the device matrices
        JCublas.cublasSetVector(size, Sizeof.FLOAT, Pointer.to(input), 1, inputPointer, 1);
        cublasSetMatrix(size, size, Sizeof.FLOAT, Pointer.to(matrix), size, matrixPointer, size);

        JCublas.cublasSetVector(
                size, Sizeof.FLOAT, Pointer.to(output), 1, outputPointer, 1);

        // Performs operation using JCublas
        JCublas.cublasSgemv(
                'n',
                size,
                size,
                1.0f,
                matrixPointer,
                size,
                inputPointer,
                1,
                0.0f,
                outputPointer,
                1);

        // Read the result back
        JCublas.cublasGetVector(
                size, Sizeof.FLOAT, outputPointer, 1, Pointer.to(output), 1);

        // Memory clean up
        JCublas.cublasFree(inputPointer);
        JCublas.cublasFree(outputPointer);
        JCublas.cublasFree(matrixPointer);

        return Tuple.tuple(output[0], output[1], output[2], output[3]);
    }
    public static CublasMatrix mulM(float[] matrixA, float[] matrixB, int size) {
        cublasHandle handle = new cublasHandle();
        cublasCreate(handle);

        JCublas2.initialize();
        float[] result = new float[size * size];

        Pointer pointerA = new Pointer();
        Pointer pointerB = new Pointer();
        Pointer pointerResult = new Pointer();

        cudaMalloc(pointerA, (long) size * size * Sizeof.FLOAT);
        cudaMalloc(pointerB, (long) size * size * Sizeof.FLOAT);
        cudaMalloc(pointerResult, (long) size * size * Sizeof.FLOAT);
        cublasSetMatrix(size, size, Sizeof.FLOAT, Pointer.to(matrixA), size, pointerA, size);
        cublasSetMatrix(size , size, Sizeof.FLOAT, Pointer.to(matrixB), size, pointerB, size);

        Pointer zero = Pointer.to(new float[]{ 0.0f });
        Pointer one = Pointer.to(new float[]{ 1.0f });
        cublasSgemm(handle, CUBLAS_OP_N, CUBLAS_OP_N, size, size, size, one,
                pointerA, size, pointerB, size, zero, pointerResult, size);

        cublasGetMatrix(size, size, Sizeof.FLOAT, pointerResult, size, Pointer.to(result), size);
        cudaFree(pointerA);
        cudaFree(pointerB);
        cudaFree(pointerResult);

        return new CublasMatrix(result, size);
    }
}
