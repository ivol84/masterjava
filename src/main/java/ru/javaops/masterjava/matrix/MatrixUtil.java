package ru.javaops.masterjava.matrix;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        return matrixC;
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        if (!isCompatible(matrixA, matrixB)) {
            throw new IllegalArgumentException();
        }
        final int matrixARowCount = matrixA.length;
        final int matrixAColCount = matrixA[0].length;
        final int matrixBColCount = matrixB[0].length;

        int[][] C = new int[matrixARowCount][matrixBColCount];
        for (int aRow = 0; aRow < matrixARowCount; aRow++) {
            for (int bCol = 0; bCol < matrixBColCount; bCol++) {
                int sum = 0;
                for (int aCol = 0; aCol < matrixAColCount; aCol++) {
                    sum += matrixA[aRow][aCol] * matrixB[aCol][bCol];
                }
                C[aRow][bCol] = sum;
            }
        }
        return C;
    }

    private static boolean isCompatible(int[][] a, int[][] b) {
        return a[0].length == b.length;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
