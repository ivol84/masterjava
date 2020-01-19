package ru.javaops.masterjava.matrix;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class MatrixUtilTest {

    @org.junit.jupiter.api.Test
    void singleThreadMultiply() {
        int[][] matrixA = new int[][] {{2, -1}, {5, 3}};
        int[][] matrixB = new int[][] {{3, 1, 0}, {2, -1, 5}};
        int[][] expectedMatrixC = new int[][] {{4, 3, -5}, {21, 2, 15}};

        int[][] matrixC = MatrixUtil.singleThreadMultiply(matrixA, matrixB);

        assertArrayEquals(expectedMatrixC, matrixC);
    }

    @Test
    void concurrentMultiply() throws ExecutionException, InterruptedException {
        int[][] matrixA = new int[][] {{2, -1}, {5, 3}};
        int[][] matrixB = new int[][] {{3, 1, 0}, {2, -1, 5}};
        int[][] expectedMatrixC = new int[][] {{4, 3, -5}, {21, 2, 15}};

        int[][] matrixC = MatrixUtil.concurrentMultiply(matrixA, matrixB, Executors.newFixedThreadPool(10));

        assertArrayEquals(expectedMatrixC, matrixC);
    }
}